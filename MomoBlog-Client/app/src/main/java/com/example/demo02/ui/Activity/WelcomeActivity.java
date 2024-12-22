package com.example.demo02.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo02.MainActivity;
import com.example.demo02.R;
import com.example.demo02.utils.Network.UserRepository;

public class WelcomeActivity extends AppCompatActivity {
    private TextView skipButton;
    private CountDownTimer countDownTimer;
    private boolean isSkipped = false; // 防止重复跳转

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        skipButton = findViewById(R.id.skip_button);

        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String text = getString(R.string.text_welcome_skip) + " " +
                        (int) Math.ceil(millisUntilFinished / 1000.0);
                skipButton.setText(text);
            }
            @Override
            public void onFinish() {
                if (!isSkipped) {
                    navigateToMain();
                }
            }
        }.start();

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSkipped = true;
                countDownTimer.cancel();
                navigateToMain();
            }
        });
    }

    private void navigateToMain() {
        Intent intent;
        UserRepository.refresh(this, getString(R.string.server_url));
        if (UserRepository.isLoggedIn(this)) {
            intent = new Intent(WelcomeActivity.this, MainActivity.class);
        } else {
            intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
