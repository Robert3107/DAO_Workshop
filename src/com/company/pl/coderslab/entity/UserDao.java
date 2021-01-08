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

    /*  Metoda "read" zwraca miejsce w pamięci. Przy próbie użycia w "main" kodu:

     UserDao userDao = new UserDao();
     User read = userDao.read(1);
     System.out.println(read);

      Metoda zwraca "com.company.pl.coderslab.entity.User@3cb1ffe6"

      Link do repozytorium: https://github.com/Robert3107/DAO_Workshop.git
      Ścieżka: DAO_Workshop/src/com/company/pl/coderslab/entity/UserDao
    */

    private static final String READ_FROM_ID = "SELECT * FROM users WHERE ID = ?;";

    public User read(int userID) {
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

}
