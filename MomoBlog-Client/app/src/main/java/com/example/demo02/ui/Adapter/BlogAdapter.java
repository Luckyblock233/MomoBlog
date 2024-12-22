package com.example.demo02.ui.Adapter;


import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demo02.R;
import com.example.demo02.entity.Blog;
import com.example.demo02.ui.Activity.DetailsActivity;
import com.example.demo02.utils.Network.UserRepository;
import com.example.demo02.utils.LikeUtils;
import com.example.demo02.utils.Network.FileDownloader;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    private List<Blog> blogList;

    public BlogAdapter(List<Blog> blogList) {
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        File directory = holder.itemView.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            try {
                // 下载文件
                FileDownloader.downloadFile(
                        holder.itemView.getContext(),
                        holder.itemView.getContext().getString(R.string.server_url),
                        directory,
                        blog.getBlogUser().toFileName(),
                        "avatars"
                );

                // 检查文件是否下载成功
                File file = new File(directory, blog.getBlogUser().toFileName());
                if (file.exists() && file.length() > 0) {
                    Log.d("Glide", "文件下载成功: " + file.getAbsolutePath());

                    // 通知主线程加载图片
                    handler.post(() -> Glide.with(holder.itemView.getContext())
                            .load(file) // 加载本地文件
                            .placeholder(R.drawable.null_avatar)
                            .error(R.drawable.null_avatar)
                            .into(holder.iv_avatar));
                } else {
                    Log.e("Glide", "文件下载失败或文件大小为 0: " + file.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("Glide", "文件下载或加载出错", e);
            }
        }).start();

        holder.tv_username.setText(blog.getBlogUser().getUsername());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm EEE");

//        Log.v("time check", blog.getBlogPostTime().toString());
        holder.tv_post_time.setText(formatter.format(blog.getBlogPostTime()));
        holder.tv_context.setText(blog.getBlogContext());

        if (blog.getBlogHaveImage()) {
            new Thread(() -> {
                try {
                    // 下载文件
                    FileDownloader.downloadFile(holder.itemView.getContext(),
                            holder.itemView.getContext().getString(R.string.server_url),
                            directory,
                            blog.toFileName(),
                            "images");

                    // 检查文件是否下载成功
                    File file = new File(directory, blog.toFileName());
                    if (file.exists() && file.length() > 0) {
                        Log.d("Glide", "文件下载成功: " + file.getAbsolutePath());

                        // 通知主线程加载图片
                        handler.post(() -> Glide.with(holder.itemView.getContext())
                                .load(new File(directory, blog.toFileName())) // 微博图片 URL
                                .placeholder(R.drawable.ic_image_gray_foreground) // 占位图
                                .error(R.drawable.ic_image_gray_foreground) // 错误图
                                .into(holder.iv_image));
                    } else {
                        Log.e("Glide", "文件下载失败或文件大小为 0: " + file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    Log.e("Glide", "文件下载或加载出错", e);
                }
            }).start();
        }
        LikeUtils.countLike(holder.itemView.getContext(), blog.getBlogId(), new LikeUtils.LikeRepositoryCountCallback() {
            @Override
            public void onSuccess(int count) {
                holder.likeCount = count;
                holder.tv_blog_like_count.setText("共有 " + holder.likeCount + " 人觉得很赞！");
            }
            @Override
            public void onFailure(String error) {
                holder.likeCount  = -1;
                holder.tv_blog_like_count.setText("共有 ? 人觉得很赞！");
            }
        });

        Log.v("blog adapter", String.valueOf(holder.likeCount));

        LikeUtils.fetchLikeExist(holder.itemView.getContext(),
                blog.getBlogId(),
                UserRepository.loggedInUser.getUserId(),
                new LikeUtils.LikeRepositoryExistCallback() {
            @Override
            public void onSuccess(Boolean exist) {
                if (exist) {
                    holder.iv_like.setImageResource(R.drawable.ic_like_pink_foreground);
                } else {
                    holder.iv_like.setImageResource(R.drawable.ic_like_foreground);
                }
            }
            @Override
            public void onFailure(String error) {
                holder.iv_like.setImageResource(R.drawable.ic_like_foreground);
                System.out.println(error);
            }
        });

        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeUtils.flipLike(holder.itemView.getContext(),
                        blog.getBlogId(),
                        UserRepository.loggedInUser.getUserId(),
                        new LikeUtils.LikeRepositoryExistCallback() {
                    @Override
                    public void onSuccess(Boolean exist) {
                        if (exist) {
                            holder.iv_like.setImageResource(R.drawable.ic_like_pink_foreground);
                        } else {
                            holder.iv_like.setImageResource(R.drawable.ic_like_foreground);
                        }

                        LikeUtils.countLike(holder.itemView.getContext(), blog.getBlogId(), new LikeUtils.LikeRepositoryCountCallback() {
                            @Override
                            public void onSuccess(int count) {
                                holder.likeCount = count;
                                holder.tv_blog_like_count.setText("共有 " + holder.likeCount + " 人觉得很赞！");
                            }
                            @Override
                            public void onFailure(String error) {
                                holder.likeCount  = -1;
                                holder.tv_blog_like_count.setText("共有 ? 人觉得很赞！");
                            }
                        });
                    }
                    @Override
                    public void onFailure(String error) {
                        System.out.println(error);
                    }
                });
            }
        });

        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                Gson gson = new Gson();
                intent.putExtra("blog", gson.toJson(blog, Blog.class));
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.tv_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                Gson gson = new Gson();
                intent.putExtra("blog", gson.toJson(blog, Blog.class));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_avatar;
        TextView tv_username, tv_post_time, tv_context, tv_blog_like_count;
        ImageView iv_image, iv_like;
        int likeCount;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.item_blog_avatar);
            tv_username = itemView.findViewById(R.id.item_blog_username);
            tv_post_time = itemView.findViewById(R.id.item_blog_post_time);
            tv_blog_like_count = itemView.findViewById(R.id.item_blog_like_count);
            tv_context = itemView.findViewById(R.id.item_blog_context);
            iv_image = itemView.findViewById(R.id.item_blog_image);
            iv_like = itemView.findViewById(R.id.item_blog_like);
            likeCount = 0;
        }
    }
}
