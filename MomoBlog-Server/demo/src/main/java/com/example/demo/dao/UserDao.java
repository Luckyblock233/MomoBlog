package com.example.demo.dao;

import com.example.demo.entity.User;

import java.util.List;

public interface UserDao {

    void insert(User user, String password) throws Exception;

    User getByUserId(int id)  throws RuntimeException;

    User getByUserName(String username)  throws RuntimeException;

    List<User> getAllUser() throws RuntimeException;

    List<User> searchUser(String keyword) throws RuntimeException;

    User login(String username, String password) throws Exception;
}
