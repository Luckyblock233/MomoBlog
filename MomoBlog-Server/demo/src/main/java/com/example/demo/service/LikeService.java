package com.example.demo.service;

import java.util.List;

public interface LikeService {
    int countBlogLikeByBlogId(Integer id) throws Exception;

    boolean checkBlogLikeByUserId(Integer blogId, Integer userId);

    List<Integer> getBlogLikeUserId(Integer id) throws Exception;

    boolean flipBlogLike(Integer blogId, Integer userId) throws Exception;
}
