package com.company;

import com.company.pl.coderslab.entity.User;
import com.company.pl.coderslab.entity.UserDao;
import java.sql.SQLException;

public class MainDao {

    public static void main(String[] args) throws SQLException {

        UserDao userDao = new UserDao();
        User user = new User();
//        user.setEmail("adama@gmail.com");
//        user.setUserName("Adam Adamowicz");
//        user.setPassword("1234");
//        userDao.create(user);

        userDao.readUser(4);
    }
}