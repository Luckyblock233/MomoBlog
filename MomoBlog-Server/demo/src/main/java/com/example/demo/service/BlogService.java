package com.example.demo.service;

import com.example.demo.entity.Blog;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BlogService {
    int insert(Blog blog) throws Exception;

    Blog getById(int id) throws RuntimeException;

    List<Blog> getByUserId(Integer id) throws RuntimeException;

    List<Blog> getAllBlog() throws RuntimeException;

    List<Blog> searchBlog(String keyword) throws RuntimeException;

    void addBlog(Integer userId, String blogContext, MultipartFile image) throws RuntimeException, IOException;

    void updateBlogLikeCount(Integer id, int delta) throws RuntimeException;
}