package com.example.demo02.entity;

public class User {
    private int userId;

    private String username;

    private int userSex;

    private String userSelfIntroduction;

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getUserSex() {
        return userSex;
    }
    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }
    public String getUserSelfIntroduction() {
        return userSelfIntroduction;
    }
    public void setUserSelfIntroduction(String userSelfIntroduction) {
        this.userSelfIntroduction = userSelfIntroduction;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userSelfIntroduction='" + userSelfIntroduction + '\'' +
//                ", userBlogs=" + userBlogs +
//                ", userComments=" + userComments +
                '}';
    }
    public String toFileName() {
        return "avatar_" + getUsername() + ".png";
    }
}
