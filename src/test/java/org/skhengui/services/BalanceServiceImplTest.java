package org.skhengui.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skhengui.models.Balance;
import org.skhengui.models.Currency;
import org.skhengui.models.User;
import org.skhengui.services.impl.BalanceServiceImpl;
import org.skhengui.services.impl.OperationServiceImpl;
import org.skhengui.services.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@DisplayName("Balance Service Impl Test")
public class BalanceServiceImplTest {
    private final UserService userService = mock(UserServiceImpl.class);
    private final OperationService operationService = mock(OperationServiceImpl.class);
    private final BalanceService balanceService =
            new BalanceServiceImpl(operationService, userService);

    private static final User USER = User.builder()
            .uuid(UUID.randomUUID())
            .firstName("firstName")
            .lastName("lastName")
            .username("username")
            .password("password")
            .balance(new Balance(BigDecimal.valueOf(5000D), Currency.EURO))
            .build();

    @Nested
    @DisplayName("Deposit")
    class Deposit {
        @Test
        @DisplayName("Should deposit the specified amount and return the updated user")
        void shouldDepositTheAmountAndReturnUpdatedUser() {
            BigDecimal amountToDeposit = BigDecimal.valueOf(2012);
            User updatedUser = balanceService.deposit(USER, amountToDeposit);
            assertAll(() -> {

                assertThat(updatedUser)
                        .usingRecursiveComparison()
                        .ignoringFields("balance")
                        .isEqualTo(USER);
                assertThat(updatedUser.balance().amount())
                        .isEqualTo(USER.balance().amount().add(amountToDeposit));
                assertThat(updatedUser.balance().currency())
                        .isEqualTo(USER.balance().currency());
            });
        }
    }

    @Nested
    @DisplayName("Withdraw")
    class Withdrawal {

        @Test
        @DisplayName("Should not withdraw any money and return the same user")
        void shouldNotWithdrawAndReturnSameUser() {
            BigDecimal amountToWithDraw = BigDecimal.valueOf(5);
            User zeroBalanceUser = User.builder(USER)
                    .balance(new Balance(BigDecimal.ZERO, Currency.EURO))
                    .build();
            User updatedUser = balanceService.withdraw(zeroBalanceUser, amountToWithDraw);
            assertAll(() -> {
                verify(userService, times(0)).save(updatedUser);
                verify(operationService, times(0)).save(any());
                assertThat(updatedUser)
                        .usingRecursiveComparison()
                        .isEqualTo(zeroBalanceUser);
            });
        }

        @Test
        @DisplayName("Should withdraw the specified amount and return the updated user")
        void shouldWithdrawTheAmountAndReturnUpdatedUser() {
            BigDecimal amountToWithDraw = BigDecimal.valueOf(421);
            User updatedUser = balanceService.withdraw(USER, amountToWithDraw);
            assertAll(() -> {
                verify(userService, times(1)).save(updatedUser);
                verify(operationService, times(1)).save(any());
                assertThat(updatedUser)
                        .usingRecursiveComparison()
                        .ignoringFields("balance")
                        .isEqualTo(USER);
                assertThat(updatedUser.balance().amount())
                        .isEqualTo(USER.balance().amount().subtract(amountToWithDraw));
                assertThat(updatedUser.balance().currency())
                        .isEqualTo(USER.balance().currency());
            });
        }
    }
}
