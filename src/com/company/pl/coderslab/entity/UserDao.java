package com.company.pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

public class UserDao {

    private static final String CREATE_DATABASE =
            "CREATE DATABASE DAO_WORKSHOP\n" +
                    "CHARACTER SET utf8mb4\n" +
                    "COLLATE utf8mb4_unicode_ci;";

    private static final String CREATE_TABLE_DAO =
            "CREATE TABLE workshop (\n" +
                    "ID int AUTO_INCREMENT not null,\n" +
                    "EMAIL varchar(255) unique not null,\n" +
                    "USER varchar(255) not null,\n" +
                    "PASSWORD varchar(60) not null,\n" +
                    "PRIMARY KEY (ID)\n" +
                    ");";

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}