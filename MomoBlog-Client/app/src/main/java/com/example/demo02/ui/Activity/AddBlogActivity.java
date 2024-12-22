package com.example.demo02.ui.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo02.R;
import com.example.demo02.utils.ImageUtils;
import com.example.demo02.utils.Network.UserRepository;
import com.example.demo02.utils.Network.HttpPostRequest;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddBlogActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextView tv_submit;
    private ImageView iv_image;
    private EditText et_editor;
    private ProgressBar progressBar;

    private final int REQUEST_CODE_PICK_IMAGE = 3; // 只在相册挑选图片的请求码
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);
        initView();
    }

    void initView() {
        iv_back = findViewById(R.id.add_blog_back);
        tv_submit = findViewById(R.id.add_blog_submit);
        iv_image = findViewById(R.id.add_blog_add_image);
        et_editor = findViewById(R.id.add_blog_et_editor);
        progressBar = findViewById(R.id.add_blog_progressBar);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_image.setOnClickListener(v -> selectImageFromGallery());

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String context = et_editor.getText().toString();
                String url = getString(R.string.server_url) + "/blog/addBlog";
                String user_id = String.valueOf(UserRepository.loggedInUser.getUserId());

                if (context.isEmpty()) {
                    Toast.makeText(AddBlogActivity.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoadingState(true);

                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", user_id)
                        .addFormDataPart("context", context);

                if (imageFile != null && imageFile.exists()) {
                    RequestBody fileBody = RequestBody.create(imageFile, MediaType.parse("image/png"));
                    builder.addFormDataPart("image", imageFile.getName(), fileBody);
                }
                RequestBody requestBody = builder.build();

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Looper.prepare();
                        Toast.makeText(AddBlogActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Looper.prepare();
                        Log.v("my_tag", response.body().string());
                        Toast.makeText(AddBlogActivity.this, "发表成功！", Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showLoadingState(false);
                                finish();
                            }
                        });
                        Looper.loop();
                    }
                });
                showLoadingState(false);
            }
        });
    }

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
                iv_image.setImageURI(selectedImageUri);
                imageFile = ImageUtils.uriToFile(getContentResolver(), getExternalFilesDir(null), selectedImageUri);
            }
        }
    }

    private void showLoadingState(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            iv_back.setEnabled(false);
            tv_submit.setEnabled(false);
            et_editor.setEnabled(false);
            iv_image.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            iv_back.setEnabled(true);
            tv_submit.setEnabled(true);
            et_editor.setEnabled(true);
            iv_image.setEnabled(true);
        }
    }
}
