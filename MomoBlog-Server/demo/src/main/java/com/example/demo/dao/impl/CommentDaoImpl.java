package com.example.demo.dao.impl;

import com.example.demo.dao.CommentDao;
import com.example.demo.entity.Blog;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.exception.BlogNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.BlogService;
import com.example.demo.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDaoImpl implements CommentDao {

    private final JdbcTemplate jdbcTemplate;

    private final UserService userService;

    private final BlogService blogService;

    public CommentDaoImpl(JdbcTemplate jdbcTemplate, UserService userService, BlogService blogService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
        this.blogService = blogService;
    }

    @Override
    public void insert(Comment comment) throws Exception {
        try {
            String sql = "insert into t_comments(commentUserId, commentBlogId, commentPostTime, commentContext) values(?,?,?,?)";
            this.jdbcTemplate.update(
                    sql,
                    comment.getCommentUser().getUserId(),
                    comment.getCommentBlog().getBlogId(),
                    comment.getCommentPostTime(),
                    comment.getCommentContext()
            );
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Comment getById(Integer id) throws RuntimeException {
        try {
            String sql = "select * from t_comments where commentId = ?";
            return this.jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
                Comment comment = new Comment();
                comment.setCommentId(resultSet.getInt("commentId"));
                comment.setCommentUser(userService.getById(resultSet.getInt("commentUserId")));
                comment.setCommentBlog(blogService.getById(resultSet.getInt("commentBlogId")));
                comment.setCommentPostTime(new java.util.Date(resultSet.getDate("commentPostTime").getTime()));
                comment.setCommentContext(resultSet.getString("commentContext"));
                return comment;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            throw new BlogNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Comment> getByUserId(Integer id) throws RuntimeException {
        User user;
        try {
            user = userService.getById(id);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        }

        String sql = "select * from t_comments where commentUserId = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Comment comment = new Comment();
            comment.setCommentId(resultSet.getInt("commentId"));
            comment.setCommentUser(userService.getById(resultSet.getInt("commentUserId")));
            comment.setCommentBlog(blogService.getById(resultSet.getInt("commentBlogId")));
            comment.setCommentPostTime(new java.util.Date(resultSet.getDate("commentPostTime").getTime()));
            comment.setCommentContext(resultSet.getString("commentContext"));
            return comment;
        }, user.getUserId());
    }

    @Override
    public List<Comment> getByBlogId(Integer id) throws RuntimeException {
        Blog blog;
        try {
            blog = blogService.getById(id);
        } catch (BlogNotFoundException e) {
            throw new BlogNotFoundException(e.getMessage());
        }

        String sql = "select * from t_comments where commentBlogId = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Comment comment = new Comment();
            comment.setCommentId(resultSet.getInt("commentId"));
            comment.setCommentUser(userService.getById(resultSet.getInt("commentUserId")));
            comment.setCommentBlog(blogService.getById(resultSet.getInt("commentBlogId")));
            comment.setCommentPostTime(new java.util.Date(resultSet.getDate("commentPostTime").getTime()));
            comment.setCommentContext(resultSet.getString("commentContext"));
            return comment;
        }, blog.getBlogId());
    }

    @Override
    public List<Comment> getAllComment() throws RuntimeException {
        String sql = "select * from t_comments";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Comment comment = new Comment();
            comment.setCommentId(resultSet.getInt("commentId"));
            comment.setCommentUser(userService.getById(resultSet.getInt("commentUserId")));
            comment.setCommentBlog(blogService.getById(resultSet.getInt("commentBlogId")));
            comment.setCommentPostTime(new java.util.Date(resultSet.getDate("commentPostTime").getTime()));
            comment.setCommentContext(resultSet.getString("commentContext"));
            return comment;
        });
    }
}