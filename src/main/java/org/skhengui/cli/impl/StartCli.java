package org.skhengui.cli.impl;

import org.skhengui.cli.Cli;
import org.skhengui.dao.SyncOnExit;
import org.skhengui.dao.UserDao;
import org.skhengui.dao.impl.UserDaoImpl;
import org.skhengui.services.UserService;
import org.skhengui.services.impl.UserServiceImpl;
import org.skhengui.utils.SynchronizeJsonData;

import java.util.Scanner;

public class StartCli implements Cli {

    private final UserDao userDao;
    private final UserService userService;

    public StartCli() {
        userDao = new UserDaoImpl();
        userDao.init();
        userService = new UserServiceImpl(userDao);
    }

    @Override
    public void display() {
        Scanner in = new Scanner(System.in);
        SynchronizeJsonData.configureDataFiles();
        while (true) {
            System.out.println("------------------------------");
            System.out.println("Welcome to Bank Wonderland");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.println("------------------------------");
            System.out.print("Please choose your operation by entering the corresponding number: ");
            int choice = in.nextInt();
            switch (choice) {
                case 1 -> new LoginCli(userDao, userService).display();
                case 2 -> new SigninCli(userService).display();
                case 3 -> {
                    SynchronizeJsonData.sync((SyncOnExit) userDao);
                    System.exit(0);
                }
                default -> System.out.println("Please choose a valid operation");
            }
        }
    }
}
