package com.example.demo02.utils.Network;

import static java.lang.Math.min;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.demo02.entity.Blog;
import com.example.demo02.entity.Comment;
import com.example.demo02.entity.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CommentRepository {
    private List<Comment> nowCommentList = new ArrayList<Comment>();
    private static final int maxPageSize = 10;
    private final int blogId;
    private static volatile boolean isDataLoaded = false; // 加载状态标志
    private static final Object lock = new Object(); // 线程同步锁

    public CommentRepository(int blogId) {
        this.blogId = blogId;
    }
    public void fetchAllComment(Context context, String url) {
        String url_comment = url + "/comment/getByBlogId";

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", String.valueOf(blogId));
        RequestBody requestBody = builder.build();

        HttpPostRequest.okhttpPost(url_comment, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("blog repository ", blogId + " " + "请求失败", e);
                synchronized (lock) {
                    isDataLoaded = true; // 即使失败也要设置为完成，防止阻塞分页逻辑
                    lock.notifyAll();
                }
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Looper.prepare();
                if (response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseData = responseBody.string();
                        Gson gson = new Gson();
                        List<Comment> commentList;

                        try {
                            commentList = gson.fromJson(responseData, new TypeToken<List<Comment>>(){}.getType());
                            if (commentList.isEmpty()) commentList = new ArrayList<Comment>();
                            Log.d("comment repository", "返回数据：" + commentList.toString());

                            synchronized (nowCommentList) {
                                nowCommentList.clear();
                                nowCommentList.addAll(commentList);
                            }
                        } catch (JsonSyntaxException e) {
                            Log.e("comment repository", "JSON 解析错误", e);
                        } catch (Exception e) {
                            Log.e("comment repository", "处理响应时出错", e);
                        } finally {
                            synchronized (lock) {
                                isDataLoaded = true;
                                lock.notifyAll(); // 通知等待的线程
                            }
                        }
                    }
                } else {
                    Log.e("comment repository", "请求失败，状态码：" + response.code());
                    synchronized (lock) {
                        isDataLoaded = true;
                        lock.notifyAll();
                    }
                }
            };
        });
    }

    public List<Comment> fetchCommentData(Context context, String url, int page) {
        if (page == 1) {
            synchronized (lock) {
                isDataLoaded = false;
                fetchAllComment(context, url);
                while (!isDataLoaded) {
                    try {
                        lock.wait(); // 等待数据加载完成
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        Log.e("comment repository", "线程被中断", e);
                    }
                }
            }
        }

        List<Comment> commentList = new ArrayList<Comment>();
        synchronized (nowCommentList) {
            for (int i = (page - 1) * maxPageSize; i < min(nowCommentList.size(), page * maxPageSize); ++ i) {
                commentList.add(nowCommentList.get(i));
            }
        }
        Log.d("comment repositoty", "分页评论数据数量：" + String.valueOf(commentList.size()));
        return commentList;
    }
}