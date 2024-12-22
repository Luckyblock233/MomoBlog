package com.example.demo.service.impl;

import com.example.demo.event.FileUploadEvent;
import com.example.demo.service.FileUploadService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final ApplicationEventPublisher eventPublisher;

    public FileUploadServiceImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    // 发布文件上传事件
    public void publishFileUploadEvent(String fileName) {
        FileUploadEvent event = new FileUploadEvent(this, fileName);
        eventPublisher.publishEvent(event);
    }
}
