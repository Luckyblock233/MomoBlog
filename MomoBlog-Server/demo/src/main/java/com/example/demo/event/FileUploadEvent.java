package com.example.demo.event;

import org.springframework.context.ApplicationEvent;

public class FileUploadEvent extends ApplicationEvent {

    private final String fileName;

    public FileUploadEvent(Object source, String fileName) {
        super(source);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
