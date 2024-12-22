package com.example.demo.dao;

import java.util.List;

public interface LikeDao {

    public int countBlogLikeByBlogId(Integer id) throws Exception;

    public int countCommentLikeByCommentId(Integer id) throws Exception;

    public boolean checkBlogLikeByUserId(Integer blogId, Integer userId);

    public boolean checkCommentLikeByUserId(Integer commentId, Integer userId);

    public List<Integer> getBlogLikeUserId(Integer id) throws Exception;

    public List<Integer> getCommentLikeUserId(Integer id) throws Exception;

    public void flipBlogLike(Integer blogId, Integer userId, boolean exist) throws Exception;

    public void flipCommentLike(Integer commentId, Integer userId, boolean exist) throws Exception;
}