package com.example.demo02.utils;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.demo02.R;
import com.example.demo02.utils.Network.HttpPostRequest;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LikeUtils {
    public static void fetchLikeExist(Context context,
                                      Integer blogId,
                                      Integer userId,
                                      final LikeRepositoryExistCallback callback) {
        String url = context.getString(R.string.server_url);
        String url_like = url + "/like/checkBlogLikeByUserId";

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("blog_id", blogId.toString())
                .addFormDataPart("user_id", userId.toString());

        RequestBody requestBody = builder.build();
        HttpPostRequest.okhttpPost(url_like, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure("网络错误，请重试");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Looper.prepare();
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    Log.d("查询是否点赞", "返回数据：" + responseData);
                    Gson gson = new Gson();
                    callback.onSuccess(gson.fromJson(responseData, Boolean.class));

                } else {
                    Log.d("查询是否点赞", "失败！返回数据：" + response.toString());
                    callback.onFailure(responseData);
                }
                Looper.loop();
            }
        });
    }

    public static void flipLike(Context context,
                                Integer blogId,
                                Integer userId,
                                final LikeRepositoryExistCallback callback) {
        fetchLikeExist(context, blogId, userId, new LikeRepositoryExistCallback() {
            @Override
            public void onSuccess(Boolean exist) {
                String url = context.getString(R.string.server_url);
                String url_like = url + "/like/flipBlogLike";

                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("blog_id", blogId.toString())
                        .addFormDataPart("user_id", userId.toString());

                RequestBody requestBody = builder.build();
                HttpPostRequest.okhttpPost(url_like, requestBody, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        callback.onFailure("网络错误，请重试");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Looper.prepare();
                        String responseData = response.body().string();
                        if (response.isSuccessful()) {
                            Log.d("翻转点赞", "返回数据：" + responseData);
                            Gson gson = new Gson();
                            callback.onSuccess(gson.fromJson(responseData, Boolean.class));

                        } else {
                            Log.d("翻转点赞", "失败！返回数据：" + response.toString());
                            callback.onFailure(responseData);
                        }
                        Looper.loop();
                    }
                });
            }
            @Override
            public void onFailure(String error) {
                callback.onFailure("错误");
            }
        });
    }
    public static void countLike(Context context, Integer blogId, LikeRepositoryCountCallback callback) {
        String url = context.getString(R.string.server_url);
        String url_like = url + "/like/countBlogLikeByBlogId";
        final int[] count = {-1};

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", blogId.toString());

        RequestBody requestBody = builder.build();
        HttpPostRequest.okhttpPost(url_like, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("查询查询数量", "查询失败！");
                callback.onFailure("查询失败");
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Looper.prepare();
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    Log.d("查询点赞数量", "返回数据：" + responseData);
                    Gson gson = new Gson();
                    callback.onSuccess(gson.fromJson(responseData, Integer.class));

                } else {
                    Log.d("查询查询数量", "失败！返回数据：" + response.toString());
                    callback.onFailure("查询失败");
                }
                Looper.loop();
            }
        });
    }
    public interface LikeRepositoryExistCallback {
        void onSuccess(Boolean exist);
        void onFailure(String error);
    }
    public interface LikeRepositoryCountCallback {
        void onSuccess(int count);
        void onFailure(String error);
    }
}
