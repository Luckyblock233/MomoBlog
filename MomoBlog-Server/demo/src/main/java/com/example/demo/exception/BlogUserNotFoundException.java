package com.example.demo.exception;

public class BlogUserNotFoundException extends RuntimeException {
    public BlogUserNotFoundException(String message) {
        super(message);
    }
}