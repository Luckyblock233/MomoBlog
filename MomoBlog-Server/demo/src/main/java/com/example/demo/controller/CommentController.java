package com.example.demo.controller;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.exception.BlogNotFoundException;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.ErrorMessages;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public ResponseEntity<?> getById(Integer id) {
        try {
            Comment comment = this.commentService.getById(id);
            return ResponseEntity.ok(comment);
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping("/getByUserId")
    @ResponseBody
    public ResponseEntity<?> getByUserId(@RequestParam Integer id) {
        try {
            List<Comment> commentList = this.commentService.getByUserId(id);
            return ResponseEntity.ok(commentList);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping("/getByBlogId")
    @ResponseBody
    public ResponseEntity<?> getByBlogId(@RequestParam Integer id) {
        try {
            List<Comment> commentList = this.commentService.getByBlogId(id);
            return ResponseEntity.ok(commentList);
        } catch (BlogNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping("/getAllComment")
    @ResponseBody
    public ResponseEntity<?> getAllBlog() {
        List<Comment> commentList = this.commentService.getAllComment();
        return ResponseEntity.ok(commentList);
    }

    @RequestMapping("/addComment")
    @ResponseBody
    public ResponseEntity<?> addBlog(
            @RequestParam("user_id") Integer userId,
            @RequestParam("blog_id") Integer blogId,
            @RequestParam("context") String commentContext) {
        try {
            commentService.addComment(userId, blogId, commentContext);

            System.out.println("add comment successful: " + commentContext);

            return ResponseEntity.ok("");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
