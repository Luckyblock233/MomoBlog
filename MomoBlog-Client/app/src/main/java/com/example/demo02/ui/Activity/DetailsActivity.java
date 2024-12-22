package com.example.demo02.ui.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.demo02.R;
import com.example.demo02.entity.Blog;
import com.example.demo02.entity.Comment;
import com.example.demo02.ui.Adapter.CommentAdapter;
import com.example.demo02.utils.LikeUtils;
import com.example.demo02.utils.Network.FileDownloader;
import com.example.demo02.utils.Network.UserRepository;
import com.example.demo02.utils.Network.CommentRepository;
import com.example.demo02.utils.Network.HttpPostRequest;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {
    ImageView iv_avatar, iv_back;
    TextView tv_username, tv_post_time, tv_context, tv_blog_like_count;
    ImageView iv_image, iv_like;
    EditText et_comment;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommentAdapter adapter;
    private final List<Comment> commentList = new ArrayList<Comment>();
    private int currentPage = 1;
    private boolean isLoading = false;
    private Button btn_comment;
    private int likeCount;
    private CommentRepository commentRepository;
    ProgressBar progressBar;
    Blog currentBlog;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initView();
    }

    void initView() {
        iv_back = findViewById(R.id.details_back);
        iv_avatar = findViewById(R.id.item_blog_avatar);
        tv_username = findViewById(R.id.item_blog_username);
        tv_post_time = findViewById(R.id.item_blog_post_time);
        tv_blog_like_count = findViewById(R.id.item_blog_like_count);
        tv_context = findViewById(R.id.item_blog_context);
        iv_image = findViewById(R.id.item_blog_image);
        iv_like = findViewById(R.id.item_blog_like);
        btn_comment = findViewById(R.id.details_btn_comment);
        et_comment = findViewById(R.id.et_comment);
        progressBar = findViewById(R.id.details_progressBar);

        Gson gson = new Gson();
        currentBlog = gson.fromJson((String) getIntent().getSerializableExtra("blog"), Blog.class);

        File directory = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            try {
                // 下载文件
                FileDownloader.downloadFile(
                        DetailsActivity.this,
                        getString(R.string.server_url),
                        directory,
                        currentBlog.getBlogUser().toFileName(),
                        "avatars"
                );

                // 检查文件是否下载成功
                File file = new File(directory, currentBlog.getBlogUser().toFileName());
                if (file.exists() && file.length() > 0) {
                    Log.d("Glide", "文件下载成功: " + file.getAbsolutePath());

                    // 通知主线程加载图片
                    handler.post(() -> Glide.with(this)
                            .load(file) // 加载本地文件
                            .placeholder(R.drawable.null_avatar)
                            .error(R.drawable.null_avatar)
                            .into(iv_avatar));
                } else {
                    Log.e("Glide", "文件下载失败或文件大小为 0: " + file.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("Glide", "文件下载或加载出错", e);
            }
        }).start();

        tv_username.setText(currentBlog.getBlogUser().getUsername());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm EEE");
        tv_post_time.setText(formatter.format(currentBlog.getBlogPostTime()));
        tv_context.setText(currentBlog.getBlogContext());

        if (currentBlog.getBlogHaveImage()) {
            new Thread(() -> {
                try {
                    // 下载文件
                    FileDownloader.downloadFile(
                            DetailsActivity.this,
                            getString(R.string.server_url),
                            directory,
                            currentBlog.toFileName(),
                            "images");

                    // 检查文件是否下载成功
                    File file = new File(directory, currentBlog.toFileName());
                    if (file.exists() && file.length() > 0) {
                        Log.d("Glide", "文件下载成功: " + file.getAbsolutePath());

                        // 通知主线程加载图片
                        handler.post(() -> Glide.with(DetailsActivity.this)
                                .load(new File(directory, currentBlog.toFileName())) // 微博图片 URL
                                .placeholder(R.drawable.ic_image_gray_foreground) // 占位图
                                .error(R.drawable.ic_image_gray_foreground) // 错误图
                                .into(iv_image));
                    } else {
                        Log.e("Glide", "文件下载失败或文件大小为 0: " + file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    Log.e("Glide", "文件下载或加载出错", e);
                }
            }).start();
        }
        refreshLikeCount();

        LikeUtils.fetchLikeExist(this,
                currentBlog.getBlogId(),
                UserRepository.loggedInUser.getUserId(),
                new LikeUtils.LikeRepositoryExistCallback() {
                    @Override
                    public void onSuccess(Boolean exist) {
                        if (exist) {
                            iv_like.setImageResource(R.drawable.ic_like_pink_foreground);
                        } else {
                            iv_like.setImageResource(R.drawable.ic_like_foreground);
                        }
                    }
                    @Override
                    public void onFailure(String error) {
                        iv_like.setImageResource(R.drawable.ic_like_foreground);
                        System.out.println(error);
                    }
                });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeUtils.flipLike(DetailsActivity.this,
                        currentBlog.getBlogId(),
                        UserRepository.loggedInUser.getUserId(),
                        new LikeUtils.LikeRepositoryExistCallback() {
                    @Override
                    public void onSuccess(Boolean exist) {
                        if (exist) {
                            iv_like.setImageResource(R.drawable.ic_like_pink_foreground);
                        } else {
                            iv_like.setImageResource(R.drawable.ic_like_foreground);
                        }
                        refreshLikeCount();
                    }
                    @Override
                    public void onFailure(String error) {
                        System.out.println(error);
                    }
                });
            }
        });

        recyclerView = findViewById(R.id.details_recyclerView);
        swipeRefreshLayout = findViewById(R.id.details_swipeRefreshLayout);

        adapter = new CommentAdapter(commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
        recyclerView.setAdapter(adapter);

        commentRepository = new CommentRepository(currentBlog.getBlogId());

        isLoading = false;
        currentPage = 1;

        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            fetchCommentData(currentPage, true);

        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true;
                    currentPage ++;
                    fetchCommentData(currentPage, false);
                }
            }
        });
        fetchCommentData(currentPage, true);

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String context = et_comment.getText().toString();
                String url = getString(R.string.server_url) + "/comment/addComment";
                String user_id = String.valueOf(UserRepository.loggedInUser.getUserId());
                String blog_id = String.valueOf(currentBlog.getBlogId());

                if (context.isEmpty()) {
                    Toast.makeText(DetailsActivity.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", user_id)
                        .addFormDataPart("blog_id", blog_id)
                        .addFormDataPart("context", context);
                RequestBody requestBody = builder.build();

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Looper.prepare();
                        Toast.makeText(DetailsActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Looper.prepare();
                        Log.v("my_tag", response.body().string());
                        Toast.makeText(DetailsActivity.this, "发表成功！请刷新后查看！", Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                et_comment.setText("");
                            }
                        });
                        Looper.loop();
                    }
                });
            }
        });
    }
    private synchronized void fetchCommentData(int page, boolean isRefresh) {
        isLoading = true;

        new Thread(() -> {
            List<Comment> newCommentList =
                    commentRepository.fetchCommentData(
                            DetailsActivity.this,
                            getString(R.string.server_url),
                            page);
            synchronized (commentList) { // 保证对 commentList 的线程安全操作
                if (isRefresh) {
                    commentList.clear();
                }
                commentList.addAll(newCommentList);
            }

            runOnUiThread(() -> {
                if (isRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;

                if (newCommentList.isEmpty()) {
                    Log.d("DetailsActivity", "没有更多数据");
                }
            });
        }).start();
    }
    void refreshLikeCount() {
        LikeUtils.countLike(DetailsActivity.this, currentBlog.getBlogId(), new LikeUtils.LikeRepositoryCountCallback() {
            @Override
            public void onSuccess(int count) {
                likeCount = count;
                tv_blog_like_count.setText("共有 " + likeCount + " 人觉得很赞！");
            }
            @Override
            public void onFailure(String error) {
                likeCount = -1;
                tv_blog_like_count.setText("共有 ? 人觉得很赞！");
            }
        });
    }
}
