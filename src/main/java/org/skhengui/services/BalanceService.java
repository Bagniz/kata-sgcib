package org.skhengui.services;

import org.skhengui.models.User;

import java.math.BigDecimal;

public interface BalanceService {
    User deposit(User user, BigDecimal amount);
    User withdraw(User user, BigDecimal amount);
}
