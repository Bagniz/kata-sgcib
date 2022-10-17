package org.skhengui.cli.impl;

import org.skhengui.cli.Cli;
import org.skhengui.dao.OperationDao;
import org.skhengui.dao.SyncOnExit;
import org.skhengui.dao.UserDao;
import org.skhengui.dao.impl.OperationDaoImpl;
import org.skhengui.services.BalanceService;
import org.skhengui.services.OperationService;
import org.skhengui.services.UserService;
import org.skhengui.services.impl.BalanceServiceImpl;
import org.skhengui.services.impl.OperationServiceImpl;
import org.skhengui.utils.SynchronizeJsonData;

import java.util.Scanner;

public class MainMenuCli implements Cli {
    private final UserDao userDao;
    private final UserService userService;
    private final OperationDao operationDao;
    private final OperationService operationService;
    private final BalanceService balanceService;

    public MainMenuCli(UserDao userDao, UserService userService) {
        this.userDao = userDao;
        this.userService = userService;
        operationDao = new OperationDaoImpl();
        operationDao.init();
        operationService = new OperationServiceImpl(operationDao);
        balanceService = new BalanceServiceImpl(operationService, userService);
    }

    @Override
    public void display() {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("------------- Main Menu -----------------");
            System.out.println("Please choose your operation: ");
            System.out.println("1. Make a withdrawal");
            System.out.println("2. Make a deposit");
            System.out.println("3. See operations history");
            System.out.println("4. Exit");
            System.out.println("------------------------------");
            System.out.print("Please choose your operation by entering the corresponding number: ");
            int choice = in.nextInt();
            switch (choice) {
                case 1 -> new WithdrawalCli(balanceService, userService).display();
                case 2 -> new DepositCli(balanceService, userService).display();
                case 3 -> new OperationHistoryCli(userService, operationService).display();
                case 4 -> {
                    SynchronizeJsonData.sync((SyncOnExit) userDao, (SyncOnExit) operationDao);
                    System.exit(0);
                }
                default -> System.out.println("Please choose a valid operation");
            }
        }
    }
}
