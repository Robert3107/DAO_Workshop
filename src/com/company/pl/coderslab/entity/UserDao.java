package com.company.pl.coderslab.entity;

import com.company.DbUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDao {

    private static final String CREATE_DATABASE =
            "CREATE DATABASE DAO_WORKSHOP\n" +
                    "CHARACTER SET UTF8MB4\n" +
                    "COLLATE UTF8MB4_UNICODE_CI;";

    private static final String CREATE_TABLE =
            "CREATE TABLE workshop (\n" +
                    "ID int AUTO_INCREMENT NOT NULL,\n" +
                    "EMAIL VARCHAR(255) UNIQUE NOT NULL,\n" +
                    "USER VARCHAR(255) NOT NULL,\n" +
                    "PASSWORD VARCHAR(60) NOT NULL,\n" +
                    "PRIMARY KEY (ID)\n" +
                    ");";

    private static final String CREATE_USER =
            "INSERT INTO users(EMAIL, USER, PASSWORD) VALUES (?, ?, ?)";
    private static final String READ_FROM_ID = "SELECT *FROM users WHERE ID = ?;";
    private static final String UPDATE_DATA_USER =
            "UPDATE users SET EMAIL = ?, USERNAME = ?, PASSWORD = ? where ID = ?;";
    private static final String DELETED_DATA_USER = "DELETE FROM users WHERE ID = ?;";
    private static final String SHOW_ALL_USERS = "SELECT *FROM users";

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) throws SQLException {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(CREATE_USER, PreparedStatement.RETURN_GENERATED_KEYS);
            {
                ResultSet resultSet = preStmt.getGeneratedKeys();
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    System.out.println("Inserted ID: " + id);
                }
            }
        }
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userID) {                                  // metoda nie zwraca prawidłowej wartosci
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_FROM_ID);
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setEmail(resultSet.getString("EMAIL"));
                user.setUserName(resultSet.getString("USER"));
                user.setPassword(resultSet.getString("PASSWORD"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}