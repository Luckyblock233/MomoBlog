package com.example.demo.dao;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Comment;

import java.util.List;

public interface CommentDao {
    
    void insert(Comment comment) throws Exception;

    Comment getById(Integer id) throws RuntimeException;

    List<Comment> getByUserId(Integer id) throws RuntimeException;

    List<Comment> getByBlogId(Integer id) throws RuntimeException;

    List<Comment> getAllComment() throws RuntimeException;
}
