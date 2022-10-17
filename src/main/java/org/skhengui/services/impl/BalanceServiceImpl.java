package org.skhengui.services.impl;

import org.skhengui.models.Balance;
import org.skhengui.models.Operation;
import org.skhengui.models.OperationType;
import org.skhengui.models.User;
import org.skhengui.services.BalanceService;
import org.skhengui.services.OperationService;
import org.skhengui.services.UserService;

import java.math.BigDecimal;
import java.time.Instant;

public class BalanceServiceImpl implements BalanceService {
    private final OperationService operationService;
    private final UserService userService;

    public BalanceServiceImpl(OperationService operationService, UserService userService) {
        this.operationService = operationService;
        this.userService = userService;
    }

    @Override
    public User deposit(User user, BigDecimal amount) {
        User updatedUser = User.builder(user)
                .balance(new Balance(user.balance().amount().add(amount), user.balance().currency()))
                .build();
        this.saveOperation(updatedUser, amount, OperationType.DEPOSIT);
        return updatedUser;
    }

    @Override
    public User withdraw(User user, BigDecimal amount) {
        Balance newBalance = new Balance(user.balance().amount().subtract(amount), user.balance().currency());

        if (newBalance.amount().doubleValue() < 0) {
            return user;
        }

        User updatedUser = User.builder(user)
                .balance(newBalance)
                .build();
        this.saveOperation(updatedUser, amount, OperationType.WITHDRAWAL);
        return updatedUser;
    }

    private void saveOperation(User updatedUser, BigDecimal amount, OperationType type) {
        this.userService.save(updatedUser);
        this.operationService.save(Operation.builder()
                .userUUID(updatedUser.uuid())
                .type(type)
                .amount(amount)
                .updatedBalance(updatedUser.balance())
                .createdAt(Instant.now())
                .build());
    }
}
