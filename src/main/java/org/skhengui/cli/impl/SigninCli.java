package org.skhengui.cli.impl;

import org.skhengui.cli.Cli;
import org.skhengui.models.User;
import org.skhengui.services.UserService;
import org.skhengui.utils.PasswordEncryptionUtils;

import java.io.Console;
import java.util.Scanner;

public class SigninCli implements Cli {
    private final UserService userService;

    public SigninCli(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void display() {
        Scanner in = new Scanner(System.in);
        Console console = System.console();
        User.Builder builder = User.builder();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("------------- SIGN IN -----------------");
        System.out.println("Please enter the following information: ");
        System.out.print("firstname: ");
        builder.firstName(in.nextLine());
        System.out.print("lastname: ");
        builder.lastName(in.nextLine());
        System.out.print("username: ");
        builder.username(in.nextLine());
        System.out.print("password: ");
        builder.password(PasswordEncryptionUtils.encrypt(new String(console.readPassword())));
        userService.save(builder.build());
        System.out.println("------------------------------");
    }
}
