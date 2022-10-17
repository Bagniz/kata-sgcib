package org.skhengui.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skhengui.dao.UserDao;
import org.skhengui.dao.impl.UserDaoImpl;
import org.skhengui.models.Balance;
import org.skhengui.models.Currency;
import org.skhengui.models.User;
import org.skhengui.services.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@DisplayName("User Service Impl Test")
public class UserServiceImplTest {
    private final UserDao userDao = mock(UserDaoImpl.class);
    private final UserService userService = new UserServiceImpl(userDao);

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final UUID USER_UUID = UUID.randomUUID();
    private static final User USER = User.builder()
            .uuid(USER_UUID)
            .firstName("firstName")
            .lastName("lastName")
            .username("username")
            .password("password")
            .balance(new Balance(BigDecimal.valueOf(5000D), Currency.EURO))
            .build();

    @Nested
    @DisplayName("Login")
    class FindUserByUsernameAndPassword {
        @Test
        @DisplayName("Should return no user for incorrect username and password")
        void shouldReturnEmptyForIncorrectUsernameAndPassword() {
            when(userDao.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.empty());
            Optional<User> actualUser = userService.login(USERNAME, PASSWORD);
            assertAll(() -> {
                verify(userDao, times(1)).findByUsernameAndPassword(USERNAME, PASSWORD);
                assertThat(actualUser).isEmpty();
            });
        }

        @Test
        @DisplayName("Should return User for correct username and password")
        void shouldReturnUserForUsernameAndPassword() {
            when(userDao.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.of(USER));
            Optional<User> actualUser = userService.login(USERNAME, PASSWORD);
            assertAll(() -> {
                verify(userDao, times(1)).findByUsernameAndPassword(USERNAME, PASSWORD);
                assertThat(actualUser).hasValue(USER);
            });
        }
    }

    @Nested
    @DisplayName("Find By Uuid")
    class FindByUuid {
        @Test
        @DisplayName("Should return no user for not existing uuid")
        void shouldReturnEmptyForNotExistingUuid() {
            UUID anotherUuid = UUID.randomUUID();
            when(userDao.findByUuid(anotherUuid)).thenReturn(Optional.empty());
            Optional<User> actualUser = userService.findByUuid(anotherUuid);
            assertAll(() -> {
                verify(userDao, times(1)).findByUuid(anotherUuid);
                assertThat(actualUser).isEmpty();
            });
        }

        @Test
        @DisplayName("Should return User for existing uuid")
        void shouldReturnUserForExistingUuid() {
            when(userDao.findByUuid(USER_UUID)).thenReturn(Optional.of(USER));
            Optional<User> actualUser = userService.findByUuid(USER_UUID);
            assertAll(() -> {
                verify(userDao, times(1)).findByUuid(USER_UUID);
                assertThat(actualUser).hasValue(USER);
            });
        }
    }

    @Nested
    @DisplayName("Save")
    class Save {
        @Test
        @DisplayName("Should call dao save method")
        void verifyCallUserDaoSave() {
            userDao.save(USER);
            verify(userDao, times(1)).save(USER);
        }
    }
}