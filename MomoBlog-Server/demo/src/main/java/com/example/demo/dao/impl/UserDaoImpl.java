package com.example.demo.dao.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(User user, String password) throws Exception {
        String userPasswordHashValue = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        try {
            String sql = "insert into t_users(userName,userPasswordHashValue,userSex,userSelfIntroduction) values(?,?,?,?)";
            this.jdbcTemplate.update(
                    sql,
                    user.getUsername(),
                    userPasswordHashValue,
                    user.getUserSex(),
                    user.getUserSelfIntroduction()
            );
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public User getByUserId(int id) throws RuntimeException {
        String sql = "select * from t_users where userId = ?";
        try {
            return this.jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("userName"));
                user.setUserSex(resultSet.getInt("userSex"));
                user.setUserSelfIntroduction(resultSet.getString("userSelfIntroduction"));
                return user;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<User> getAllUser() throws RuntimeException {
        try {
            String sql = "select * from t_users";
            return jdbcTemplate.query(sql, (resultSet, i) -> {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("userName"));
                user.setUserSex(resultSet.getInt("userSex"));
                user.setUserSelfIntroduction(resultSet.getString("userSelfIntroduction"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

    @Override
    public User getByUserName(String username) throws RuntimeException {
        String sql = "select * from t_users where userName = ?";
        try {
            return this.jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("userName"));
                user.setUserSex(resultSet.getInt("userSex"));
                user.setUserSelfIntroduction(resultSet.getString("userSelfIntroduction"));
                return user;
            }, username);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<User> searchUser(String keyword) throws RuntimeException {
        try {
            String sql = "select * from t_users where userName like ?";
            return jdbcTemplate.query(sql, (resultSet, i) -> {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("userName"));
                user.setUserSex(resultSet.getInt("userSex"));
                user.setUserSelfIntroduction(resultSet.getString("userSelfIntroduction"));
                return user;
            }, "%" + keyword + "%");
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

    @Override
    public User login(String username, String password) {
        String passwordHashValue = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        try {
            String sql = "select * from t_users where userName=? and userPasswordHashValue=?";
            return this.jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("userName"));
                user.setUserSex(resultSet.getInt("userSex"));
                user.setUserSelfIntroduction(resultSet.getString("userSelfIntroduction"));
                return user;
            }, username,passwordHashValue);
        } catch (Exception e) {
            throw new InvalidPasswordException(e.getMessage());
        }
    }
}

