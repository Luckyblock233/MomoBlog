package com.example.demo.service;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    void insert(com.example.demo.entity.Comment comment) throws Exception;

    Comment getById(Integer id) throws RuntimeException;

    List<Comment> getByUserId(Integer id) throws RuntimeException;

    List<Comment> getByBlogId(Integer id) throws RuntimeException;

    List<Comment> getAllComment() throws RuntimeException;

    void addComment(Integer userId, Integer blogId, String commentContext) throws RuntimeException, IOException;
}