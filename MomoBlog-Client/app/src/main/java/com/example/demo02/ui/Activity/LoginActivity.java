package com.example.demo02.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo02.MainActivity;
import com.example.demo02.R;
import com.example.demo02.utils.Network.UserRepository;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }
    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        String url = getString(R.string.server_url);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "用户名、密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                showLoadingState(true);

                UserRepository.login(LoginActivity.this, url, username, password, new UserRepository.LoginCallback() {
                    @Override
                    public void onLoginSuccess() {
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    @Override
                    public void onLoginFailure(String error) {
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });

                showLoadingState(false);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showLoadingState(boolean isLoading) {
        if (isLoading) {
            // 显示进度条，禁用输入框和按钮
            progressBar.setVisibility(View.VISIBLE);
            btn_login.setEnabled(false);
            btn_register.setEnabled(false);
            et_username.setEnabled(false);
            et_password.setEnabled(false);
        } else {
            // 隐藏进度条，启用输入框和按钮
            progressBar.setVisibility(View.GONE);
            btn_login.setEnabled(true);
            btn_register.setEnabled(true);
            et_username.setEnabled(true);
            et_password.setEnabled(true);
        }
    }
}
