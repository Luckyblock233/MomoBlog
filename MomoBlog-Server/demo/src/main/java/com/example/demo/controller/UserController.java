package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.exception.ErrorMessages;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public ResponseEntity<?> getById(int id) {
        try {
            User user = this.userService.getById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @RequestMapping("/getByUserName")
    @ResponseBody
    public ResponseEntity<?> getByUserName(String username) {
        try {
            User user = this.userService.getByUserName(username);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @RequestMapping("/searchUser")
    @ResponseBody
    public ResponseEntity<?> searchUser(String keyword) {
        try {
            List<User> userList = this.userService.searchUser(keyword);
            return ResponseEntity.ok(userList);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public ResponseEntity<?> getAllUser() {
        try {
            List<User> userList = this.userService.getAllUser();
            return ResponseEntity.ok(userList);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @RequestMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(String username, String password) {

        System.out.println(username + " " + password);

        try {
            User user = this.userService.login(username, password);

            System.out.println("user login in: " + user.toString());

            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.USER_NOT_FOUND);
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessages.INVALID_PASSWORD);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.DATABASE_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestParam("username") String userName,
            @RequestParam("password") String password,
            @RequestParam("sex") String userSex,
            @RequestParam("introduction") String userSelfIntroduction,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        try {
            User user = new User();
            user.setUsername(userName);
            user.setUserSex(Integer.parseInt(userSex));
            user.setUserSelfIntroduction(userSelfIntroduction);

            userService.registerUser(user, password, avatar);
            System.out.println("register successful: " + user);

            return ResponseEntity.ok(user);
        } catch (UserExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.USER_EXiSTS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.DATABASE_ERROR);
        }
    }
}