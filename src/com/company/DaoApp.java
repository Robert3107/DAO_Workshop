package com.company;

import com.company.pl.coderslab.entity.User;
import com.company.pl.coderslab.entity.UserDao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class DaoApp {

    private static final String NAME_APP = "DAO DATABASE";
    private static final String EXIT = "0";
    private static final String CREATE = "1";
    private static final String READ_ID = "2";
    private static final String SHOW_ALL = "3";
    private static final String UPDATE_DATA = "4";
    private static final String DELETED_USER = "5";

    public void runApp() throws SQLException {

        UserDao userDao = new UserDao();
        User user = new User();

        printStart();
        Scanner scr = new Scanner(System.in);
        while (scr.hasNext()) {
            String option = scr.nextLine();

            switch (option) {

                case CREATE:
                    userDao.create(user);
                    break;
                case READ_ID:
                    userDao.readUser();
                    break;
                case SHOW_ALL:
                    User[] users = userDao.showAll();
                    String print= Arrays.toString(users);
                    System.out.println(print);
                    break;
                case UPDATE_DATA:
                    userDao.update(user);
                    break;
                case DELETED_USER:
                    userDao.deleted();
                    break;
                case EXIT:
                    System.out.println("Koniec programu");
                    System.exit(0);
                default:
                    System.err.println("Wybierz właściwą opcję");
            }
            printStart();
        }
        scr.close();
    }

    public static void printStart() {
        String option = "\n" + "----------" + NAME_APP + "----------" + "\n" + "\n"
                + CREATE + " - Utwórz nowego użytkownika" + "\n"
                + READ_ID + " - Wyświetl użytkownika" + "\n"
                + SHOW_ALL + " - Wyświetl wszystkich użytkowników" + "\n"
                + UPDATE_DATA + " - Zaktualizuj użytkownika" + "\n"
                + DELETED_USER + " - Usuń użytkownika" + "\n"
                + EXIT + " - Zakończ program";
        System.out.println(option);
    }
}
