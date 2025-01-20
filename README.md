# MomoBlog

## Intro

A weibo-like social software for Application Development Practice II

Based on SpringBoot + Mysql.

![](./some%20pic/sample.jpg)

## How to use

To understand the functions, please refer to document `.\doc`。

The title comes from mobile game: Blue Archive, and thanks to support of my waifu Mari.

To init the software, You should:
- enter your IP of your own device in string `<string name="server_url">  </string>`, in the file: `.\MomoBlog-Client\app\src\main\res\values\styles.xml`;
- init your database:
```mysql
create table t_users (
    userId int NOT NULL AUTO_INCREMENT primary key ,
    userName varchar(20) not null,
    userPasswordHashValue varchar(32) not null,
    userSex int,
    userSelfIntroduction varchar(200)
);

create table t_blogs (
    blogId int NOT NULL AUTO_INCREMENT primary key ,
    blogUserId int not null,
    blogPostTime datetime,
    blogContext varchar(200),
    blogLikeCount int,
    blogHaveImage boolean
);

create table t_comments (
    commentId int NOT NULL AUTO_INCREMENT primary key ,
    commentUserId int not null,
    commentBlogId int not null,
    commentPostTime datetime,
    commentLikeCount int,
    commentContext varchar(200)
);

create table t_likes_blog (
    likeBlogId int not null auto_increment primary key ,
    likeBlogUserId int,
    likeBlogBlogId int
);
```
Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.

Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.Kill me plz.

---

---

---

---

---

---

---

---

# Kill me.

# PLZ.
