package com.company;

import com.company.pl.coderslab.entity.User;
import com.company.pl.coderslab.entity.UserDao;

import java.sql.SQLException;


public class MainDao {

    public static void main(String[] args) throws SQLException {

        UserDao userDao = new UserDao();
        User user = new User();

        user.setEmail("arkadiusz.jozwiak@coderslab.pl");
        user.setUserName("Arkadiusz Nowak");
        user.setPassword("qwerty123");

        System.out.println(userDao.read(user.getId()));
    }
}

