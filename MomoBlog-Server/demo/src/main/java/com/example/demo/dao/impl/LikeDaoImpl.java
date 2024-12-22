package com.example.demo.dao.impl;

import com.example.demo.dao.LikeDao;
import com.example.demo.entity.Blog;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.exception.BlogNotFoundException;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.BlogService;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    private final UserService userService;

    private final BlogService blogService;

    private final CommentService commentService;


    public LikeDaoImpl(JdbcTemplate jdbcTemplate, UserService userService, BlogService blogService, CommentService commentService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
        this.blogService = blogService;
        this.commentService = commentService;
    }

    @Override
    public int countBlogLikeByBlogId(Integer id) throws Exception {
        try {
            String sql = "select count(*) from t_likes_blog where likeBlogBlogId = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new BlogNotFoundException(e.getMessage());
        }
    }

    @Override
    public int countCommentLikeByCommentId(Integer id) throws Exception {
        try {
            String sql = "select count(*) from t_likes_comment where likeCommentCommentId = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new CommentNotFoundException(e.getMessage());
        }
    }

    @Override
    public boolean checkBlogLikeByUserId(Integer blogId, Integer userId) {
        String sql = "select exists (select 1 from t_likes_blog where likeBlogBlogId = ? and likeBlogUserId = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, blogId, userId));
    }

    @Override
    public boolean checkCommentLikeByUserId(Integer commentId, Integer userId) {
        String sql = "select exists (select 1 from t_likes_comment where likeCommentCommentId = ? and likeCommentUserId = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, commentId, userId));
    }

    @Override
    public List<Integer> getBlogLikeUserId(Integer id) throws Exception {
        String sql = "select * from t_likes_blog where likeBlogBlogId = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getInt("likeBlogUserId"), id);
    }

    @Override
    public List<Integer> getCommentLikeUserId(Integer id) throws Exception {
        String sql = "select * from t_likes_comment where likeCommentCommentId = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getInt("likeCommentUserId"), id);
    }

    @Override
    public void flipBlogLike(Integer blogId, Integer userId, boolean exist) throws Exception {
        if (exist) {
            String sql = "delete from t_likes_blog where likeBlogBlogId = ? and likeBlogUserId = ?";
            jdbcTemplate.update(sql, blogId, userId);
        } else {
            String sql = "insert into t_likes_blog(likeBlogBlogId, likeBlogUserId) values(?,?)";
            jdbcTemplate.update(sql, blogId, userId);
        }
    }

    @Override
    public void flipCommentLike(Integer commentId, Integer userId, boolean exist) throws Exception {
        if (exist) {
            String sql = "delete from t_likes_comment where likeCommentCommentId = ? and likeCommentUserId = ?";
            jdbcTemplate.update(sql, commentId, userId);
        } else {
            String sql = "insert into t_likes_comment(likeCommentCommentId, likeCommentUserId) values(?,?)";
            jdbcTemplate.update(sql, commentId, userId);
        }
    }
}
