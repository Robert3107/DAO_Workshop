package com.company.pl.coderslab.entity;

import com.company.DbUtil;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.Arrays;

public class UserDao {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private static final String CREATE_USER = "INSERT INTO users(EMAIL, USER, PASSWORD) VALUES (?, ?, ?)";
    public User create(User user) throws SQLException {
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
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                System.out.println("Inserted ID: " + id);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*  Method "read" return place in memory. If try using method in "main".:
        UserDao userDao = new UserDao();
        User read = userDao.read(1);
        System.out.println(read);
        Return method and print in console "com.company.pl.coderslab.entity.User@3cb1ffe6"
    */

    private static final String READ_FROM_ID = "SELECT * FROM users WHERE ID = ?;";
    public User read(int userID) throws SQLException {
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
    public void readUser(int ID) throws SQLException {
        UserDao userDao = new UserDao();
        System.out.println("ID: " + userDao.read(ID).getId() + "\n" +
                "Email: " + userDao.read(ID).getEmail() + "\n" +
                "User: " + userDao.read(ID).getUserName() + "\n" +
                "Password: " + userDao.read(ID).getPassword());
    }

    private static final String UPDATE_DATA_USER = "UPDATE users SET EMAIL = ?, USER= ?, PASSWORD = ? where ID = ?;";
    public void update(User user) throws SQLException {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_DATA_USER);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, this.hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String DELETED_DATA_USER = "DELETE FROM users WHERE ID = ?;";
    public void deleted(int userID) throws SQLException {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETED_DATA_USER);
            statement.setInt(1, userID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String SHOW_ALL_USERS = "SELECT * FROM users";
    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }

    //Method "showAll" return place in memory.
    public User[] showAll() throws SQLException {
        try (Connection conn = DbUtil.getConnection()) {
            User[] users = new User[0];
            PreparedStatement statement = conn.prepareStatement(SHOW_ALL_USERS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setEmail(resultSet.getString("EMAIL"));
                user.setUserName(resultSet.getString("USER"));
                user.setPassword(resultSet.getString("PASSWORD"));
                users = addToArray(user, users);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}