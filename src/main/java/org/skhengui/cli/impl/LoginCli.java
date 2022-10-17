package org.skhengui.cli.impl;

import org.skhengui.cli.Cli;
import org.skhengui.dao.UserDao;
import org.skhengui.services.UserService;
import org.skhengui.utils.PasswordEncryptionUtils;
import org.skhengui.utils.UserThreadLocal;

import java.io.Console;
import java.util.Scanner;

public class LoginCli implements Cli {

    private final UserDao userDao;
    private final UserService userService;

    public LoginCli(UserDao userDao, UserService userService) {
        this.userDao = userDao;
        this.userService = userService;
    }

    @Override
    public void display() {
        Scanner in = new Scanner(System.in);
        Console console = System.console();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("------------- LOGGIN IN -----------------");
        System.out.print("Username: ");
        String username = in.nextLine();
        System.out.print("Password: ");
        String password = PasswordEncryptionUtils.encrypt(new String(console.readPassword()));
        this.userService.login(username, password).ifPresentOrElse((user -> {
            new UserThreadLocal().setCurrentUserUuid(user.uuid());
            new MainMenuCli(userDao, userService).display();
        }), () -> {
            System.out.println("Wrong username/password");
        });
        System.out.println("------------------------------");
    }
}
