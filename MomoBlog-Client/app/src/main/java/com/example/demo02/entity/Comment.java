package com.example.demo02.entity;

import java.util.Date;

public class Comment {
    private int commentId;

    private User commentUser;

    private Blog commentBlog;

    private Date commentPostTime;

    private String commentContext;

    public int getCommentId() {
        return commentId;
    }
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
    public User getCommentUser() {
        return commentUser;
    }
    public void setCommentUser(User commentUser) {
        this.commentUser = commentUser;
    }
    public Blog getCommentBlog() {
        return commentBlog;
    }
    public void setCommentBlog(Blog commentBlog) {
        this.commentBlog = commentBlog;
    }
    public Date getCommentPostTime() {
        return commentPostTime;
    }
    public void setCommentPostTime(Date commentPostTime) {
        this.commentPostTime = commentPostTime;
    }
    public String getCommentContext() {
        return commentContext;
    }
    public void setCommentContext(String commentContext) {
        this.commentContext = commentContext == null ? "" : commentContext;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + commentId +
                ", commentUser='" + commentUser.toString() + '\'' +
                ", commentBlog='" + commentBlog.toString() + '\'' +
                ", commentPostTime='" + commentPostTime.toString() + '\'' +
                ", commentContext=" + commentContext +
//                ", userBlogs=" + userBlogs +
//                ", userComments=" + userComments +
                '}';
    }
}
