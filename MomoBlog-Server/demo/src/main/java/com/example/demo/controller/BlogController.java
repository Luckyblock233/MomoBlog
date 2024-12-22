package com.example.demo.controller;

import com.example.demo.entity.Blog;
import com.example.demo.exception.BlogNotFoundException;
import com.example.demo.exception.ErrorMessages;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public ResponseEntity<?> getById(Integer id) {
        try {
            Blog blog = this.blogService.getById(id);
            return ResponseEntity.ok(blog);
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.BLOG_NOT_FOUND);
        }
    }

    @RequestMapping("/getByUserId")
    @ResponseBody
    public ResponseEntity<?> getByUserId(@RequestParam Integer id) {
        try {
            List<Blog> blogList = this.blogService.getByUserId(id);
            return ResponseEntity.ok(blogList);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.USER_NOT_FOUND);
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.BLOG_NOT_FOUND);
        }
    }

    @RequestMapping("/getAllBlog")
    @ResponseBody
    public ResponseEntity<?> getAllBlog() {
        List<Blog> blogList = this.blogService.getAllBlog();
        return ResponseEntity.ok(blogList);
    }

    @RequestMapping("/searchBlog")
    @ResponseBody
    public ResponseEntity<?> searchBlog(String keyword) {
        List<Blog> blogList = this.blogService.searchBlog(keyword);
        return ResponseEntity.ok(blogList);
    }

    @RequestMapping("/addBlog")
    @ResponseBody
    public ResponseEntity<?> addBlog(
            @RequestParam("user_id") Integer userId,
            @RequestParam("context") String blogContext,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            blogService.addBlog(userId, blogContext, image);
            
            System.out.println("add blog successful: " + blogContext);

            return ResponseEntity.ok("");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.USER_NOT_FOUND);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.DATABASE_ERROR);
        }
    }
}
