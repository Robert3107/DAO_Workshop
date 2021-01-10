package com.company.pl.coderslab.entity;

import com.company.DbUtil;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class UserDao {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    Scanner scr = new Scanner(System.in);

    private static final String CREATE_USER = "INSERT INTO users(EMAIL, USER, PASSWORD) VALUES (?, ?, ?)";

    public User create(User user) throws SQLException {
        System.out.println("Podaj email: ");
        String email = scr.nextLine();
        System.out.println("Podaj nazwę użytkownika: ");
        String userName = scr.nextLine();
        System.out.println("Podaj hasło: ");
        String password = scr.nextLine();
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, email);
            statement.setString(2, userName);
            statement.setString(3, hashPassword(password));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                System.out.println("Dodano nowego użytkownika " + "\n" + "Nowe ID: " + id);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

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
    public void readUser() throws SQLException {

        System.out.println("Podaj ID użytkownika: ");
        int userID = scr.nextInt();
        UserDao userDao = new UserDao();
        System.out.println("ID: " + userDao.read(userID).getId() + "\n" +
                "Email: " + userDao.read(userID).getEmail() + "\n" +
                "Użytkownik: " + userDao.read(userID).getUserName() + "\n" +
                "Hasło: " + userDao.read(userID).getPassword()
        );
    }

    private static final String UPDATE_DATA_USER = "UPDATE users SET EMAIL = ?, USER= ?, PASSWORD = ? where ID = ?;";

    public void update(User user) throws SQLException {
        System.out.println("Podaj nowy email: ");
        String newEmail = scr.nextLine();
        System.out.println("Podaj nową nazwę użytkownika: ");
        String newUserName = scr.nextLine();
        System.out.println("Podaj nowe hasło: ");
        String newPassword = scr.nextLine();
        System.out.println("Podaj ID użytkownika do aktualizacji: ");
        int userID = scr.nextInt();

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_DATA_USER);
            statement.setString(1, newEmail);
            statement.setString(2, newUserName);
            statement.setString(3, this.hashPassword(newPassword));
            statement.setInt(4, userID);
            statement.executeUpdate();
            System.out.println("Użytkownik zaktualizowany.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String DELETED_DATA_USER = "DELETE FROM users WHERE ID = ?;";

    public void deleted() throws SQLException {
        System.out.println("Podaj ID użytkownika: ");
        int userID = scr.nextInt();
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETED_DATA_USER);
            statement.setInt(1, userID);
            statement.executeUpdate();
            System.out.println("Użytkownik usunięty");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }

    private static final String SHOW_ALL_USERS = "SELECT * FROM users";
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