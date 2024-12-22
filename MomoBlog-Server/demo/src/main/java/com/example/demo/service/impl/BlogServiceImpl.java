package com.example.demo.service.impl;

import com.example.demo.dao.BlogDao;
import com.example.demo.entity.Blog;
import com.example.demo.entity.User;
import com.example.demo.exception.BlogNotFoundException;
import com.example.demo.exception.ErrorMessages;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.BlogService;
import com.example.demo.service.FileUploadService;
import com.example.demo.service.UserService;
import com.example.demo.values.ConstMessages;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogDao blogDao;

    private final UserService userService;

    private final FileUploadService fileUploadService;


    public BlogServiceImpl(BlogDao blogDao, UserService userService, FileUploadService fileUploadService) {
        this.blogDao = blogDao;
        this.userService = userService;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public int insert(Blog blog) throws IOException {
        try {
            return blogDao.insert(blog);
        } catch (Exception e) {
            throw new IOException(ErrorMessages.INSERT_ERROR);
        }
    }

    @Override
    public Blog getById(int id) throws RuntimeException {
        try {
            return blogDao.getById(id);
        } catch (BlogNotFoundException e) {
            throw new BlogNotFoundException(ErrorMessages.BLOG_NOT_FOUND);
        }
    }

    @Override
    public List<Blog> getByUserId(Integer id) throws RuntimeException {
        try {
            userService.getById(id);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }

        try {
            return blogDao.getByUserId(id);
        } catch (BlogNotFoundException e) {
            throw new BlogNotFoundException(ErrorMessages.BLOG_NOT_FOUND);
        }
    }

    @Override
    public List<Blog> getAllBlog() throws RuntimeException {
        return blogDao.getAllBlog();
    }

    @Override
    public List<Blog> searchBlog(String keyword) throws RuntimeException {
        return blogDao.searchBlog(keyword);
    }

    @Override
    public void addBlog(Integer userId, String blogContext, MultipartFile image) throws IOException {
        Blog blog = new Blog();
        try {
            User user = userService.getById(userId);
            blog.setBlogUser(user);
            blog.setBlogContext(blogContext);
            blog.setBlogPostTime(new Date());
            blog.setBlogHaveImage(image != null && !image.isEmpty());

            blog.setBlogId(insert(blog));
        } catch (UserNotFoundException e) {
          throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
        } catch (Exception e) {
            throw new IOException(ErrorMessages.INSERT_ERROR);
        }

        try {
            if (blog.getBlogHaveImage()) {
                String fileName = "image_" + blog.getBlogId() + ".png";
                Path imagePath = Paths.get(ConstMessages.uploadBlogImageDir, fileName);

                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, image.getBytes());
                fileUploadService.publishFileUploadEvent(fileName);
            }

        } catch (IOException e) {
            throw new IOException(ErrorMessages.IMAGE_FILE_SAVE_ERROR);
        }
    }

    @Override
    public void updateBlogLikeCount(Integer id, int delta) throws RuntimeException {
        try {
            blogDao.updateBlogLikeCount(id, delta);
        } catch (BlogNotFoundException e) {
            throw new BlogNotFoundException(ErrorMessages.BLOG_NOT_FOUND);
        }
    }
}
