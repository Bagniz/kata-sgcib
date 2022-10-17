package org.skhengui.cli.impl;

import org.skhengui.cli.Cli;
import org.skhengui.models.Operation;
import org.skhengui.models.User;
import org.skhengui.services.OperationService;
import org.skhengui.services.UserService;
import org.skhengui.utils.UserThreadLocal;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class OperationHistoryCli implements Cli {
    private final UserThreadLocal userThreadLocal = new UserThreadLocal();
    private final UserService userService;
    private final OperationService operationService;

    public OperationHistoryCli(UserService userService, OperationService operationService) {
        this.userService = userService;
        this.operationService = operationService;
    }

    @Override
    public void display() {
        User user = userService.findByUuid(userThreadLocal.getCurrentUserUuid())
                .orElseThrow(() -> new NoSuchElementException("User with uuid " + userThreadLocal.getCurrentUserUuid() + " not found"));
        List<Operation> operations = operationService.getOperationsForUserWithUuid(user.uuid());
        Scanner in = new Scanner(System.in);
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("------------- OPERATION HISTORY -----------------");
        System.out.println("Operations history for user " + user.username() + " : ");
        if (operations.isEmpty()) {
            System.out.println("Operation history is empty");
        } else {
            operations.stream()
                    .sorted(Comparator.comparing(Operation::createdAt).reversed())
                    .forEach(System.out::println);
        }
        System.out.println("------------------------------");
    }
}
