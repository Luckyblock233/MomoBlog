package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.exception.ErrorMessages;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.FileUploadService;
import com.example.demo.service.UserService;
import com.example.demo.values.ConstMessages;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final FileUploadService fileUploadService;

    public UserServiceImpl(UserDao userDao, FileUploadService fileUploadService) {
        this.userDao = userDao;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public void insert(User user, String password) throws Exception{
        try {
            userDao.insert(user, password);
        } catch (Exception e) {
            throw new Exception(ErrorMessages.INSERT_ERROR);
        }
    }

    @Override
    public User getById(int id) throws RuntimeException {
        try {
            return userDao.getByUserId(id);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @Override
    public User getByUserName(String userName) throws RuntimeException{
        try {
            return userDao.getByUserName(userName);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @Override
    public List<User> getAllUser() throws RuntimeException {
        try {
            return userDao.getAllUser();
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @Override
    public List<User> searchUser(String keyword) throws RuntimeException {
        try {
            return userDao.searchUser(keyword);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @Override
    public User login(String username, String password) throws Exception {
        try {
            getByUserName(username);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        }

        try {
            return userDao.login(username, password);
        } catch (InvalidPasswordException e) {
            throw new InvalidPasswordException(ErrorMessages.INVALID_PASSWORD);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void registerUser(User user, String password, MultipartFile avatar) throws Exception {
        try {
            userDao.getByUserName(user.getUsername());

            System.out.println("exception user exists " + user.getUsername());

            throw new UserExistException(ErrorMessages.USER_EXiSTS);
        } catch (UserNotFoundException e) {
            try {
                insert(user, password);
            } catch (Exception e1) {
                throw new IOException(ErrorMessages.INSERT_ERROR);
            }

            try {
                if (avatar != null && !avatar.isEmpty()) {
                    String fileName = "avatar_" + user.getUsername() + ".png";
                    Path avatarPath = Paths.get(ConstMessages.uploadAvatarDir, fileName);

                    Files.createDirectories(avatarPath.getParent());
                    Files.write(avatarPath, avatar.getBytes());
                    fileUploadService.publishFileUploadEvent(fileName);
                }
            } catch (IOException e1) {
                throw new IOException(ErrorMessages.AVATAR_FILE_SAVE_ERROR);
            }
        }
    }
}

