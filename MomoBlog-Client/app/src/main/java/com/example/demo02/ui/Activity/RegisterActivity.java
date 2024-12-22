package com.example.demo02.ui.Activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.demo02.R;
import com.example.demo02.utils.ImageUtils;
import com.example.demo02.utils.Network.UserRepository;

import java.io.File;
public class RegisterActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private int choose_sex;
    private EditText et_intro;

    private ImageView iv_avatar; // 声明一个图像视图对象
    private final int REQUEST_CODE_PICK_IMAGE = 3; // 只在相册挑选图片的请求码

    private File imageFile;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    // 打开系统相册选择图片
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // 预览图片
                iv_avatar.setImageURI(selectedImageUri);

                // 将 Uri 转换为文件
                imageFile = ImageUtils.uriToFile(getContentResolver(), getExternalFilesDir(null), selectedImageUri);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        //绑定控件
        et_username = findViewById(R.id.edit_username);
        et_password = findViewById(R.id.edit_pwd);
        Spinner spinner_sex = findViewById(R.id.spinner_sex);
        et_intro = findViewById(R.id.edit_intro);
        Button btn_register = findViewById(R.id.btn_register);

        String[] items = {"男", "女", "不知道", "沃尔玛购物袋"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sex.setAdapter(adapter);

        spinner_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_sex = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                choose_sex = 0;
            }
        });

        iv_avatar = findViewById(R.id.iv_avatar);
        iv_avatar.setOnClickListener(v -> selectImageFromGallery());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回按钮
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_foreground); // 可选，自定义返回按钮图标
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserRepository.register(
                        RegisterActivity.this,
                        getString(R.string.server_url),
                        et_username.getText().toString(),
                        et_password.getText().toString(),
                        choose_sex,
                        et_intro.getText().toString(),
                        imageFile,
                        new UserRepository.RegisterCallBack() {
                            @Override
                            public void onRegisterSuccess() {
                                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            @Override
                            public void onRegisterFailure(String error) {
                                Toast.makeText(RegisterActivity.this, "注册失败：" + error, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
