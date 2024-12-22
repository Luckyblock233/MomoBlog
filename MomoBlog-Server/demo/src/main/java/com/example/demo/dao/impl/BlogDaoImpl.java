package com.example.demo.dao.impl;

import com.example.demo.entity.Blog;

import com.example.demo.dao.BlogDao;
import com.example.demo.exception.BlogNotFoundException;
import com.example.demo.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class BlogDaoImpl implements BlogDao {

    private final JdbcTemplate jdbcTemplate;

    private final UserService userService;

    public BlogDaoImpl(JdbcTemplate jdbcTemplate, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
    }

    @Override
    public int insert(Blog blog) throws Exception {
        try {
            String sql = "insert into t_blogs(blogUserId,blogPostTime,blogContext,blogLikeCount,blogHaveImage) values(?,?,?,?,?)";
            this.jdbcTemplate.update(
                    sql,
                    blog.getBlogUser().getUserId(),
                    blog.getBlogPostTime(),
                    blog.getBlogContext(),
                    0,
                    blog.getBlogHaveImage()
            );
            sql = "select max(blogId) from t_blogs";
            return this.jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public Blog getById(int id) throws RuntimeException {
        try {
            String sql = "select * from t_blogs where blogId = ?";
            return this.jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setBlogUser(userService.getById(resultSet.getInt("blogUserId")));
                blog.setBlogPostTime(new java.util.Date(resultSet.getTimestamp("blogPostTime").getTime()));
                blog.setBlogContext(resultSet.getString("blogContext"));
                blog.setBlogLikeCount(resultSet.getInt("blogLikeCount"));
                blog.setBlogHaveImage(resultSet.getBoolean("blogHaveImage"));
                return blog;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            throw new BlogNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Blog> getByUserId(Integer id) throws RuntimeException {
        try {
            String sql = "select * from t_blogs where blogUserId = ?";
            return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setBlogUser(userService.getById(resultSet.getInt("blogUserId")));
                blog.setBlogPostTime(new java.util.Date(resultSet.getTimestamp("blogPostTime").getTime()));
                blog.setBlogContext(resultSet.getString("blogContext"));
                blog.setBlogLikeCount(resultSet.getInt("blogLikeCount"));
                blog.setBlogHaveImage(resultSet.getBoolean("blogHaveImage"));
                return blog;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            throw new BlogNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Blog> searchBlog(String keyword) throws RuntimeException {
        String sql = "select * from t_blogs where t_blogs.blogContext like ?";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Blog blog = new Blog();
            blog.setBlogId(resultSet.getInt("blogId"));
            blog.setBlogUser(userService.getById(resultSet.getInt("blogUserId")));
            blog.setBlogPostTime(new java.util.Date(resultSet.getTimestamp("blogPostTime").getTime()));
            blog.setBlogContext(resultSet.getString("blogContext"));
            blog.setBlogLikeCount(resultSet.getInt("blogLikeCount"));
            blog.setBlogHaveImage(resultSet.getBoolean("blogHaveImage"));
            return blog;
        }, "%" + keyword + "%");
    }

    @Override
    public List<Blog> getAllBlog() throws RuntimeException {
        String sql = "select * from t_blogs";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Blog blog = new Blog();
            blog.setBlogId(resultSet.getInt("blogId"));
            blog.setBlogUser(userService.getById(resultSet.getInt("blogUserId")));
            blog.setBlogPostTime(new java.util.Date(resultSet.getTimestamp("blogPostTime").getTime()));
            blog.setBlogContext(resultSet.getString("blogContext"));
            blog.setBlogLikeCount(resultSet.getInt("blogLikeCount"));
            blog.setBlogHaveImage(resultSet.getBoolean("blogHaveImage"));
            return blog;
        });
    }

    @Override
    public void updateBlogLikeCount(Integer id, int delta) throws RuntimeException {
        try {
            String sql = "update t_blogs set blogLikeCount = blogLikeCount + (?) where blogId = ?";
            this.jdbcTemplate.update(sql, delta, id);
        } catch (EmptyResultDataAccessException e) {
            throw new BlogNotFoundException(e.getMessage());
        }
    }
}
