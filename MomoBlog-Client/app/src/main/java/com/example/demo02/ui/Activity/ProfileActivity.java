package com.example.demo02.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.demo02.R;
import com.example.demo02.utils.Network.UserRepository;
import com.example.demo02.utils.Network.FileDownloader;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {
    private ImageView iv_back;
    private ShapeableImageView siv_avatar;
    private TextView tv_username;
    private TextView tv_intro;
    private ScrollView scrollView;
    private TextView tv_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
    }
    void initView() {
        iv_back = findViewById(R.id.profile_back);
        siv_avatar = findViewById(R.id.profile_avatar);
        tv_username = findViewById(R.id.profile_username);
        tv_intro = findViewById(R.id.profile_intro);
        scrollView = findViewById(R.id.profile_scroll_view);
        tv_logout = findViewById(R.id.profile_logout);

        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            try {
                // 下载文件
                FileDownloader.downloadFile(
                        ProfileActivity.this,
                        getString(R.string.server_url),
                        directory,
                        UserRepository.loggedInUser.toFileName(),
                        "avatars");

                // 检查文件是否下载成功
                File file = new File(directory, UserRepository.loggedInUser.toFileName());
                if (file.exists() && file.length() > 0) {
                    Log.d("Glide", "文件下载成功: " + file.getAbsolutePath());

                    // 通知主线程加载图片
                    handler.post(() -> Glide.with(this)
                            .load(new File(directory, UserRepository.loggedInUser.toFileName())) // 用户头像 URL
                            .placeholder(R.drawable.null_avatar) // 占位图
                            .error(R.drawable.null_avatar) // 错误图
                            .into(siv_avatar));
                } else {
                    Log.e("Glide", "文件下载失败或文件大小为 0: " + file.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("Glide", "文件下载或加载出错", e);
            }
        }).start();

        tv_username.setText(UserRepository.loggedInUser.getUsername());
        tv_intro.setText(UserRepository.loggedInUser.getUserSelfIntroduction());

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRepository.logout(ProfileActivity.this);
                Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}