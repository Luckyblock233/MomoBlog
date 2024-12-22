package com.example.demo.service.impl;

import com.example.demo.dao.LikeDao;
import com.example.demo.exception.BlogNotFoundException;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.ErrorMessages;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.BlogService;
import com.example.demo.service.CommentService;
import com.example.demo.service.LikeService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeDao likeDao;

    private final UserService userService;

    private final BlogService blogService;

    private final CommentService commentService;

    public LikeServiceImpl(LikeDao likeDao, UserService userService, BlogService blogService, CommentService commentService) {
        this.likeDao = likeDao;
        this.userService = userService;
        this.blogService = blogService;
        this.commentService = commentService;
    }

    @Override
    public int countBlogLikeByBlogId(Integer id) throws Exception {
        try {
            return likeDao.countBlogLikeByBlogId(id);
        } catch (BlogNotFoundException e) {
            throw new BlogNotFoundException(ErrorMessages.BLOG_NOT_FOUND);
        }
    }

    @Override
    public boolean checkBlogLikeByUserId(Integer blogId, Integer userId) {
        return likeDao.checkBlogLikeByUserId(blogId, userId);
    }

    @Override
    public List<Integer> getBlogLikeUserId(Integer id) throws Exception {
        try {
            blogService.getById(id);
        } catch (BlogNotFoundException e) {
            throw new BlogNotFoundException(e.getMessage());
        }

        try {
            return likeDao.getBlogLikeUserId(id);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public boolean flipBlogLike(Integer blogId, Integer userId) throws Exception {
        try {
            blogService.getById(blogId);
        } catch (BlogNotFoundException e) {
            throw new BlogNotFoundException(e.getMessage());
        }

        try {
            userService.getById(userId);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        }

        try {
            boolean exist = checkBlogLikeByUserId(blogId, userId);
            likeDao.flipBlogLike(blogId, userId, exist);
            blogService.updateBlogLikeCount(blogId, (exist ? -1 : 1));
            return !exist;
        } catch (Exception e) {
            throw new IOException(ErrorMessages.DATABASE_ERROR);
        }
    }
}