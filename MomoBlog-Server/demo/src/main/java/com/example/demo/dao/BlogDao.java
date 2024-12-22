package com.example.demo.dao;

import com.example.demo.entity.Blog;

import java.util.List;

public interface BlogDao {

    int insert(Blog blog) throws Exception;

    Blog getById(int id) throws RuntimeException;

    List<Blog> getByUserId(Integer id) throws RuntimeException;

    List<Blog> searchBlog(String keyword) throws RuntimeException;

    List<Blog> getAllBlog() throws RuntimeException;

    void updateBlogLikeCount(Integer id, int delta) throws RuntimeException;
}
