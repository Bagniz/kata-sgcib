package org.skhengui.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skhengui.dao.UserDao;
import org.skhengui.dao.impl.UserDaoImpl;
import org.skhengui.models.Balance;
import org.skhengui.models.Currency;
import org.skhengui.models.User;
import org.skhengui.services.UserService;
import org.skhengui.services.impl.UserServiceImpl;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Login In IT")
public class LoginIT {
    private UserDao userDao = new UserDaoImpl();
    private UserService userService = new UserServiceImpl(userDao);

    private static final User USER = User.builder()
            .firstName("firstName")
            .lastName("lastName")
            .username("username")
            .password("password")
            .balance(new Balance(BigDecimal.valueOf(5000D), Currency.EURO))
            .build();

    @BeforeEach
    void setup() {
        userDao = new UserDaoImpl();
        userService = new UserServiceImpl(userDao);
        userService.save(USER);
    }

    @Test
    @DisplayName("Should return existing user for username and password")
    void shouldReturnUserForUsernameAndPassword() {
        assertThat(userService.login(USER.username(), USER.password()))
                .isNotEmpty()
                .map(User::username)
                .hasValue(USER.username());
    }

    @Test
    @DisplayName("Should return existing user for username and password")
    void shouldReturnEmptyForWrongUsernameAndPassword() {
        assertThat(userService.login("differentUsername", "differentPassword"))
                .isEmpty();
    }
}
