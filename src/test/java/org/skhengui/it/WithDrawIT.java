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

@DisplayName("Withdraw IT")
public class WithDrawIT {
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
    @DisplayName("Does not withdraw the specified amount nor historize the operation")
    void shouldNotWithdrawTheAmount() {
        BigDecimal amount = BigDecimal.valueOf(324);
        Optional<User> user = userService.login(USER.username(), USER.password());
        assertThat(user).isNotEmpty();
        User updatedUser = balanceService.withdraw(user.get(), amount);
        Optional<User> savedUser = userService.findByUuid(updatedUser.uuid());
        List<Operation> operationList = operationService.getOperationsForUserWithUuid(updatedUser.uuid());
        assertAll(() -> {
            assertThat(savedUser).hasValue(updatedUser);
            assertThat(updatedUser.balance().amount())
                    .isEqualTo(USER.balance().amount());
            assertThat(updatedUser.balance().currency())
                    .isEqualTo(USER.balance().currency());
            assertThat(operationList.size()).isEqualTo(0);
        });
    }

    @Test
    @DisplayName("Withdraw the specified amount and historize the operation")
    void shouldWithdrawTheAmount() {
        BigDecimal depositedAmount = BigDecimal.valueOf(1000);
        BigDecimal amount = BigDecimal.valueOf(324);
        Optional<User> user = userService.login(USER.username(), USER.password());
        assertThat(user).isNotEmpty();
        User updatedUser  = balanceService.deposit(user.get(), depositedAmount);
        User actualUser = balanceService.withdraw(updatedUser, amount);
        Optional<User> savedUser = userService.findByUuid(updatedUser.uuid());
        List<Operation> operationList = operationService.getOperationsForUserWithUuid(updatedUser.uuid());
        Operation operation = operationList.get(1);
        assertAll(() -> {
            assertThat(savedUser).hasValue(actualUser);
            assertThat(actualUser.balance().amount())
                    .isEqualTo(depositedAmount.subtract(amount));
            assertThat(actualUser.balance().currency())
                    .isEqualTo(USER.balance().currency());
            assertThat(operationList.size()).isEqualTo(2);
            assertThat(operation.amount())
                    .isEqualTo(amount);
            assertThat(operation.updatedBalance())
                    .isEqualTo(new Balance(depositedAmount.subtract(amount), Currency.EURO));
            assertThat(operation.type())
                    .isEqualTo(OperationType.WITHDRAWAL);
            assertThat(operation.createdAt()).isCloseTo(Instant.now(), within(1, ChronoUnit.SECONDS));
        });
    }
}
