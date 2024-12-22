package com.example.demo02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.demo02.ui.Activity.AddBlogActivity;
import com.example.demo02.ui.Fragment.HomeFragment;
import com.example.demo02.ui.Activity.ProfileActivity;
import com.example.demo02.ui.Activity.SearchActivity;
import com.example.demo02.ui.Fragment.WhatshotFragment;
import com.example.demo02.utils.Network.UserRepository;
import com.example.demo02.utils.Network.FileDownloader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView siv_user_avatar;
    private ConstraintLayout search_box;
    private BottomNavigationView btm_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        siv_user_avatar = findViewById(R.id.user_avatar);
        search_box = findViewById(R.id.search_box);
        btm_nav = findViewById(R.id.bottom_navigation);

        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            try {
                // 下载文件
                FileDownloader.downloadFile(MainActivity.this,
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
                            .into(siv_user_avatar));
                } else {
                    Log.e("Glide", "文件下载失败或文件大小为 0: " + file.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("Glide", "文件下载或加载出错", e);
            }
        }).start();
        loadFragment(new HomeFragment());

        btm_nav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_add:
                    Intent intent = new Intent(MainActivity.this, AddBlogActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.nav_whatshot:
                    selectedFragment = new WhatshotFragment();
                    break;
            }
            return loadFragment(selectedFragment);
        });

        siv_user_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        search_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
