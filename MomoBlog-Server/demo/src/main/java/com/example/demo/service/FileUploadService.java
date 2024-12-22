package com.example.demo.service;

import org.springframework.context.ApplicationEventPublisher;

public interface FileUploadService {
    public void publishFileUploadEvent(String fileName);
}