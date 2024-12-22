package com.example.demo02.ui.Adapter;

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
import com.example.demo02.entity.Comment;
import com.example.demo02.utils.Network.FileDownloader;
import com.example.demo02.utils.Network.UserRepository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        File directory = holder.itemView.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            try {
                // 下载文件
                FileDownloader.downloadFile(
                        holder.itemView.getContext(),
                        holder.itemView.getContext().getString(R.string.server_url),
                        directory,
                        comment.getCommentUser().toFileName(),
                        "avatars"
                );

                // 检查文件是否下载成功
                File file = new File(directory, comment.getCommentUser().toFileName());
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

        holder.tv_username.setText(comment.getCommentUser().getUsername());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm EEE");
        holder.tv_post_time.setText(formatter.format(comment.getCommentPostTime()));
        holder.tv_context.setText(comment.getCommentContext());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_avatar;
        TextView tv_username, tv_post_time, tv_context;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.item_comment_avatar);
            tv_username = itemView.findViewById(R.id.item_comment_username);
            tv_post_time = itemView.findViewById(R.id.item_comment_post_time);
            tv_context = itemView.findViewById(R.id.item_comment_context);
        }
    }
}
