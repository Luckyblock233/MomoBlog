package com.example.demo.listener;

import com.example.demo.event.FileUploadEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FileUploadListener {

    @EventListener
    public void onFileUpload(FileUploadEvent event) {
        String fileName = event.getFileName();
        System.out.println("File uploaded: " + fileName);
    }
}
