package com.example.demo02.ui.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo02.R;

public class SearchActivity extends AppCompatActivity {
    private ImageView iv_back;
    private EditText et_search;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }
    void initView() {
        iv_back = findViewById(R.id.search_back);
        et_search = findViewById(R.id.search_edit_text);
        scrollView = findViewById(R.id.search_scroll_view);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
