package org.skhengui.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skhengui.dao.UserDao;
import org.skhengui.dao.impl.UserDaoImpl;
import org.skhengui.exceptions.UserAlreadyExistsException;
import org.skhengui.models.Balance;
import org.skhengui.models.Currency;
import org.skhengui.models.User;
import org.skhengui.services.UserService;
import org.skhengui.services.impl.UserServiceImpl;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Sign In IT")
public class SiginIT {
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
    }

    @Test
    @DisplayName("User already exists")
    void shouldThrowException() {
        userService.save(USER);
        assertThatThrownBy(() -> {
            userService.save(USER);
        }).isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with username " + USER.username() + " already exists");
    }

    @Test
    @DisplayName("User signed in")
    void shouldSignInUser() {
        userService.save(USER);
        assertThat(userService.login(USER.username(), USER.password()))
                .isNotEmpty()
                .map(User::username)
                .hasValue(USER.username());
    }
}
