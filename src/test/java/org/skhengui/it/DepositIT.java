package org.skhengui.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skhengui.dao.OperationDao;
import org.skhengui.dao.UserDao;
import org.skhengui.dao.impl.OperationDaoImpl;
import org.skhengui.dao.impl.UserDaoImpl;
import org.skhengui.models.*;
import org.skhengui.services.BalanceService;
import org.skhengui.services.OperationService;
import org.skhengui.services.UserService;
import org.skhengui.services.impl.BalanceServiceImpl;
import org.skhengui.services.impl.OperationServiceImpl;
import org.skhengui.services.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Deposit IT")
public class DepositIT {
    private UserService userService;
    private OperationService operationService;
    private BalanceService balanceService;

    private static final User USER = User.builder()
            .firstName("firstName")
            .lastName("lastName")
            .username("username")
            .password("password")
            .balance(new Balance(BigDecimal.ZERO, Currency.EURO))
            .build();


    @BeforeEach
    void setup() {
        UserDao userDao = new UserDaoImpl();
        OperationDao operationDao = new OperationDaoImpl();
        userService = new UserServiceImpl(userDao);
        operationService = new OperationServiceImpl(operationDao);
        balanceService = new BalanceServiceImpl(operationService, userService);
        userService.save(USER);
    }

    @Test
    @DisplayName("Deposit the specified amount and historize the operation")
    void shouldDepositTheAmount() {
        BigDecimal amount = BigDecimal.valueOf(13123);
        Optional<User> user = userService.login(USER.username(), USER.password());
        assertThat(user).isNotEmpty();
        User updatedUser = balanceService.deposit(user.get(), amount);
        Optional<User> savedUser = userService.findByUuid(updatedUser.uuid());
        List<Operation> operationList = operationService.getOperationsForUserWithUuid(updatedUser.uuid());
        Operation operation = operationList.get(0);
        assertAll(() -> {
            assertThat(savedUser).hasValue(updatedUser);
            assertThat(updatedUser.balance().amount())
                    .isEqualTo(USER.balance().amount().add(amount));
            assertThat(updatedUser.balance().currency())
                    .isEqualTo(USER.balance().currency());
            assertThat(operationList.size()).isEqualTo(1);
            assertThat(operation.amount())
                    .isEqualTo(amount);
            assertThat(operation.updatedBalance())
                    .isEqualTo(new Balance(USER.balance().amount().add(amount), Currency.EURO));
            assertThat(operation.type())
                    .isEqualTo(OperationType.DEPOSIT);
            assertThat(operation.createdAt()).isCloseTo(Instant.now(), within(1, ChronoUnit.MILLIS));
        });
    }
}
