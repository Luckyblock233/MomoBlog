package com.example.demo.service.impl;

import com.example.demo.dao.BlogDao;
import com.example.demo.dao.CommentDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.entity.Blog;
import com.example.demo.exception.BlogNotFoundException;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.ErrorMessages;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.BlogService;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    private final UserService userService;

    private final BlogService blogService;

    public CommentServiceImpl(CommentDao commentDao, UserService userService, BlogService blogService) {
        this.commentDao = commentDao;
        this.userService = userService;
        this.blogService = blogService;
    }

    @Override
    public void insert(Comment comment) throws Exception {
        try {
            commentDao.insert(comment);
        } catch (Exception e) {
            throw new Exception(ErrorMessages.INSERT_ERROR);
        }
    }

    @Override
    public Comment getById(Integer id) throws RuntimeException {
        try {
            return commentDao.getById(id);
        } catch (BlogNotFoundException e) {
            throw new CommentNotFoundException(ErrorMessages.COMMENT_NOT_FOUND);
        }
    }

    @Override
    public List<Comment> getByUserId(Integer id) throws RuntimeException {
        try {
            return commentDao.getByUserId(id);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @Override
    public List<Comment> getByBlogId(Integer id) throws RuntimeException {
        try {
            return commentDao.getByBlogId(id);
        } catch (BlogNotFoundException e) {
            throw new BlogNotFoundException(ErrorMessages.BLOG_NOT_FOUND);
        }
    }

    @Override
    public List<Comment> getAllComment() throws RuntimeException {
        return commentDao.getAllComment();
    }

    @Override
    public void addComment(Integer userId, Integer blogId, String commentContext) throws RuntimeException, IOException {
        Comment comment = new Comment();
        try {
            User user = userService.getById(userId);
            comment.setCommentUser(user);

            Blog blog = blogService.getById(blogId);
            comment.setCommentBlog(blog);
            comment.setCommentPostTime(new Date());

            comment.setCommentContext(commentContext);
            blog.setBlogPostTime(new Date());

            insert(comment);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
        } catch (BlogNotFoundException e) {
            throw new BlogNotFoundException(ErrorMessages.BLOG_NOT_FOUND);
        } catch (Exception e) {
            throw new IOException(ErrorMessages.INSERT_ERROR);
        }
    }
}