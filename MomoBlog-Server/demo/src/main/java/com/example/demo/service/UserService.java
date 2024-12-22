package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    void insert(User user, String password) throws Exception;

    User getById(int id) throws RuntimeException;

    User getByUserName(String username) throws RuntimeException;

    User login(String username, String password) throws Exception;

    List<User> getAllUser() throws RuntimeException;

    List<User> searchUser(String keyword) throws RuntimeException;

    void registerUser(User user, String password, MultipartFile avatar) throws Exception;
}