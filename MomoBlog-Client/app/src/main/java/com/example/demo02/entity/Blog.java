package com.example.demo02.entity;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Blog {
    private int blogId;

    private User blogUser;

    private Date blogPostTime;

    private String blogContext;

    private Boolean blogHaveImage;
    private int blogLikeCount;
    static public Comparator<Blog> sortByPostTime = new Comparator<Blog>() {
        @Override
        public int compare(Blog p1, Blog p2) {
            return -Long.compare(p1.getBlogPostTime().getTime(), p2.getBlogPostTime().getTime());
        }
    };
    static public Comparator<Blog> sortByLikeCount = new Comparator<Blog>() {
        @Override
        public int compare(Blog p1, Blog p2) {
            if (p1.getBlogLikeCount() != p2.getBlogLikeCount()) {
                return -Integer.compare(p1.getBlogLikeCount(), p2.getBlogLikeCount());
            }
            return -Long.compare(p1.getBlogPostTime().getTime(), p2.getBlogPostTime().getTime());
        }
    };

    public int getBlogId() {
        return this.blogId;
    }
    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }
    public User getBlogUser() {
        return blogUser;
    }
    public void setBlogUser(User blogUser) {
        this.blogUser = blogUser;
    }
    public Date getBlogPostTime() {
        return blogPostTime;
    }
    public void setBlogPostTime(Date blogPostTime) {
        this.blogPostTime = blogPostTime;
    }
    public String getBlogContext() {
        return blogContext;
    }
    public void setBlogContext(String blogContext) {
        this.blogContext = blogContext;
    }
    public Boolean getBlogHaveImage() {
        return blogHaveImage;
    }
    public void setBlogHaveImage(Boolean blogHaveImage) {
        this.blogHaveImage = blogHaveImage;
    }
    public int getBlogLikeCount() {
        return blogLikeCount;
    }
    public void setBlogLikeCount(int blogLikeCount) {
        this.blogLikeCount = blogLikeCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + blogId +
                ", blogUser='" + blogUser.toString() + '\'' +
                ", blogPostTime='" + blogPostTime.toString() + '\'' +
                ", blogContext='" + blogContext + '\'' +
                ", blogHaveImage=" + blogHaveImage +
//                ", userBlogs=" + userBlogs +
//                ", userComments=" + userComments +
                '}';
    }
    public String toFileName() {
        return "image_" + getBlogId() + ".png";
    }
}
