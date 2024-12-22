package com.example.demo02.utils.Network;

import com.example.demo02.R;
import com.example.demo02.entity.User;
import com.example.demo02.ui.Activity.RegisterActivity;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserRepository {
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    public static User loggedInUser = null;

    // 保存登录状态
    public static void setLoggedIn(Context context, boolean isLoggedIn, User user) {
        loggedInUser = user;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.putInt(KEY_USER_ID, user.getUserId());
        editor.apply();
    }

    // 检查是否已登录
    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public static Integer getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }
    public static void login(
            Context context,
            String url,
            String username,
            String password,
            final LoginCallback callback) {
        String url_login = url + "/user/login";

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password);

        RequestBody requestBody = builder.build();
        HttpPostRequest.okhttpPost(url_login, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onLoginFailure("网络错误，请重试");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Looper.prepare();
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    Log.d("Login", "返回数据：" + responseData);
                    Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    setLoggedIn(context, true, gson.fromJson(responseData, User.class));
                    callback.onLoginSuccess();

                } else {
                    Log.d("Login", "失败！返回数据：" + response.toString());
                    callback.onLoginFailure(responseData);
                }
                Looper.loop();
            }
        });
    }
    public static void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        loggedInUser = null;
        editor.clear();
        editor.apply();
    }
    public static void refresh(Context context, String url) {
        if (!isLoggedIn(context)) return ;

        String url_user = url + "/user/getById";
        Integer id = getUserId(context);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", String.valueOf(id));

        RequestBody requestBody = builder.build();
        HttpPostRequest.okhttpPost(url_user, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.prepare();
                Log.e("getUserById", "getUserById 请求失败：" + e.getMessage());
                logout(context);
                Looper.loop();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Looper.prepare();
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    Log.d("Login refresh", "返回数据：" + responseData);
                    Gson gson = new Gson();
                    User currentUser = gson.fromJson(responseData, User.class);
                    setLoggedIn(context, true, currentUser);

                } else {
                    Log.d("Login refresh", "失败！返回数据：" + responseData);
                    Toast.makeText(context, responseData, Toast.LENGTH_SHORT).show();
                    logout(context);
                }
                Looper.loop();
            }
        });
    }
    public static void register(
            Context context,
            String url,
            String username,
            String password,
            Integer sex,
            String intro,
            File avatar,
            final RegisterCallBack callback) {
        String url_user = url + "/user/register";

        //请求传入的参数
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .addFormDataPart("sex", sex.toString())
                .addFormDataPart("introduction", intro);

        if (avatar != null && avatar.exists()) {
            RequestBody fileBody = RequestBody.create(avatar, MediaType.parse("image/jpeg"));
            builder.addFormDataPart("avatar", avatar.getName(), fileBody);
        } else {
            callback.onRegisterFailure("请选择头像！");
            return ;
        }
        RequestBody requestBody = builder.build();

        HttpPostRequest.okhttpPost(url_user, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.prepare();
                callback.onRegisterFailure("网络错误！");
                Looper.loop();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Looper.prepare();
                String responseData = response.body().string();
                Log.v("register ", responseData);

                if (response.isSuccessful()) {
                    callback.onRegisterSuccess();
                } else {
                    callback.onRegisterFailure(responseData + "！");
                }
                Looper.loop();
            }
        });
    }
    public interface LoginCallback {
        void onLoginSuccess();
        void onLoginFailure(String error);
    }
    public interface RegisterCallBack {
        void onRegisterSuccess();

        void onRegisterFailure(String error);
    }
}