package com.example.demo.controller;

import com.example.demo.dao.impl.LikeDaoImpl;
import com.example.demo.exception.BlogNotFoundException;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.ErrorMessages;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.LikeService;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService, LikeDaoImpl likeDaoImpl, UserServiceImpl userServiceImpl) {
        this.likeService = likeService;
    }

    @RequestMapping("/countBlogLikeByBlogId")
    @ResponseBody
    public ResponseEntity<?> countBlogLikeByBlogId(Integer id) {
        try {
            int cnt = likeService.countBlogLikeByBlogId(id);
            return ResponseEntity.ok(cnt);
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.BLOG_NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.DATABASE_ERROR);
        }
    }

    @RequestMapping("/checkBlogLikeByUserId")
    @ResponseBody
    public ResponseEntity<?> checkBlogLikeByUserId(
            @RequestParam("blog_id") Integer blogId,
            @RequestParam("user_id") Integer userId) {
        return ResponseEntity.ok(likeService.checkBlogLikeByUserId(blogId, userId));
    }

    @RequestMapping("/getBlogLikeUserId")
    @ResponseBody
    public ResponseEntity<?> getBlogLikeUserId(Integer id) {
        try {
            List<Integer> userIdList = likeService.getBlogLikeUserId(id);
            return ResponseEntity.ok(userIdList);
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.BLOG_NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.DATABASE_ERROR);
        }
    }

    @RequestMapping("/flipBlogLike")
    @ResponseBody
    public ResponseEntity<?> flipBlogLike(
            @RequestParam("blog_id") Integer blogId,
            @RequestParam("user_id") Integer userId) {
        try {
            return ResponseEntity.ok(likeService.flipBlogLike(blogId, userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.USER_NOT_FOUND);
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.BLOG_NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.DATABASE_ERROR);
        }
    }
}
