package com.example.demo02.utils.Network;

import static java.lang.Math.min;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.demo02.entity.Blog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BlogRepository {
    private final List<Blog> nowBlogList = new CopyOnWriteArrayList<>();
    private final int maxPageSize = 10;
    private final ExecutorService executor = Executors.newFixedThreadPool(5); // 限制并发数
    private volatile boolean isDataLoaded = false; // 加载状态标志
    private final Object lock = new Object(); // 线程同步锁

    private final Comparator<Blog> comparator;
    public BlogRepository(String sortBy) {
        if (sortBy.equals("sort_by_likeCount")) {
            comparator = Blog.sortByLikeCount;
        } else {
            comparator = Blog.sortByPostTime;
        }
    }

    public void fetchAllBlog(Context context, String url) {
        String url_blog = url + "/blog/getAllBlog";

        HttpGetRequest.sendOkHttpGetRequest(url_blog, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("blog repository", "请求失败", e);
                synchronized (lock) {
                    isDataLoaded = true; // 即使失败也要设置为完成，防止阻塞分页逻辑
                    lock.notifyAll();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseData = responseBody.string();
                        Gson gson = new Gson();
                        List<Blog> blogList;

                        try {
                            blogList = gson.fromJson(responseData, new TypeToken<List<Blog>>() {}.getType());
                            if (blogList == null) blogList = new ArrayList<>();
                            blogList.sort(comparator);;
                            Log.d("blog repository", blogList.toString());

                            // 下载图片
                            CountDownLatch latch = new CountDownLatch(blogList.size());
                            for (Blog blog : blogList) {
                                executor.submit(() -> {
                                    try {
                                        checkImageDownloaded(context, url, blog);
                                    } finally {
                                        latch.countDown();
                                    }
                                });
                            }
                            latch.await(); // 等待所有图片下载完成

                            synchronized (nowBlogList) {
                                nowBlogList.clear();
                                nowBlogList.addAll(blogList);
                            }
                        } catch (JsonSyntaxException e) {
                            Log.e("blog repository", "JSON 解析错误", e);
                        } catch (Exception e) {
                            Log.e("blog repository", "处理响应时出错", e);
                        } finally {
                            synchronized (lock) {
                                isDataLoaded = true;
                                lock.notifyAll(); // 通知等待的线程
                            }
                        }
                    }
                } else {
                    Log.e("blog repository", "请求失败，状态码：" + response.code());
                    synchronized (lock) {
                        isDataLoaded = true;
                        lock.notifyAll();
                    }
                }
            }
        });
    }
    public List<Blog> fetchBlogData(Context context, String url, int page) {
        if (page == 1) {
            synchronized (lock) {
                isDataLoaded = false;
                fetchAllBlog(context, url);
                while (!isDataLoaded) {
                    try {
                        lock.wait(); // 等待数据加载完成
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        Log.e("blog repository", "线程被中断", e);
                    }
                }
            }
        }

        List<Blog> blogList = new ArrayList<>();
        synchronized (nowBlogList) {
            for (int i = (page - 1) * maxPageSize; i < Math.min(nowBlogList.size(), page * maxPageSize); i++) {
                blogList.add(nowBlogList.get(i));
            }
        }
        Log.d("blog repository", "分页博客数据数量：" + blogList.size());
        return blogList;
    }

    private void checkImageDownloaded(Context context, String url, Blog blog) {
        File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (FileDownloader.checkFileExists(directory, blog.toFileName()) &&
            FileDownloader.checkFileExists(directory, blog.getBlogUser().toFileName())) {
            return ;
        }

        try {
            FileDownloader.downloadFile(context, url, directory, blog.toFileName(), "images");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileDownloader.downloadFile(context, url, directory, blog.getBlogUser().toFileName(), "avatars");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
