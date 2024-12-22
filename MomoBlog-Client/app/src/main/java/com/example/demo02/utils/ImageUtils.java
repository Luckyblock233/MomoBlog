package com.example.demo02.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageUtils {
    static public File uriToFile(ContentResolver contentResolver, File externalFileDir, Uri uri) {
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            File file = new File(externalFileDir, "temp_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[2000000];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.close();
            inputStream.close();

            return file;
        } catch (Exception e) {
            Log.e("my_tag", "Error converting URI to File: " + e.getMessage());
            return null;
        }
    }
}
