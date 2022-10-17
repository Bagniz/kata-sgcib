package org.skhengui.cli.impl;

import org.skhengui.cli.Cli;
import org.skhengui.models.User;
import org.skhengui.services.BalanceService;
import org.skhengui.services.UserService;
import org.skhengui.utils.UserThreadLocal;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DepositCli implements Cli {
    private final BalanceService balanceService;
    private final UserService userService;
    private final UserThreadLocal userThreadLocal = new UserThreadLocal();

    public DepositCli(BalanceService balanceService, UserService userService) {
        this.balanceService = balanceService;
        this.userService = userService;
    }

    @Override
    public void display() {
        User user = userService.findByUuid(userThreadLocal.getCurrentUserUuid())
                .orElseThrow(() -> new NoSuchElementException("User with uuid " + userThreadLocal.getCurrentUserUuid() + " not found"));
        Scanner in = new Scanner(System.in);
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("------------- DEPOSIT -----------------");
        System.out.println("Current balance is: " + user.balance().amount() + " " + user.balance().currency().symbol);
        System.out.print("Please enter the amount to deposit: ");
        User updatedUser = this.balanceService.deposit(user, BigDecimal.valueOf(in.nextDouble()));
        System.out.println("New balance is: " + updatedUser.balance().amount() + " " + updatedUser.balance().currency().symbol);
        System.out.println("------------------------------");
    }
}
