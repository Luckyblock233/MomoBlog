package com.example.demo02.utils.Network;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileDownloader {
    private static final OkHttpClient client = new OkHttpClient();

    static public boolean checkFileExists(File directory, String fileName) {
        File file = new File(directory, fileName);
        return file.exists() && file.length() > 0; // 确保文件大小大于 0
    }
    public static void downloadFile(Context context,
                                    String url,
                                    File saveDirectory,
                                    String fileName,
                                    String type) throws Exception {
        if (checkFileExists(saveDirectory, fileName)) return ;

        String fileUrl = url + "/upload/" + type + "/" + fileName;
        Request request = new Request.Builder().url(fileUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                File saveFile = new File(saveDirectory, fileName);  // 构建保存的文件路径

                try (InputStream inputStream = response.body().byteStream();
                     OutputStream outputStream = new FileOutputStream(saveFile)) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("图片已保存: " + saveFile.getAbsolutePath());
                }
            } else {
                System.out.println("下载图片失败: " + " " + fileUrl + " " + fileName + " " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
