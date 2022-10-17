package org.skhengui.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.skhengui.dao.SyncOnExit;
import org.skhengui.dao.UserDao;
import org.skhengui.exceptions.UserAlreadyExistsException;
import org.skhengui.models.Balance;
import org.skhengui.models.Currency;
import org.skhengui.models.User;
import org.skhengui.utils.DataConstants;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class UserDaoImpl implements UserDao, SyncOnExit {

    private final List<User> users;
    private final ObjectMapper mapper;

    public UserDaoImpl() {
        users = Collections.synchronizedList(new ArrayList<>());
        mapper = JsonMapper.builder().findAndAddModules().build();
    }

    @Override
    public void init() {
        File usersJson = new File(DataConstants.JSON_USERS_FILE);
        if (usersJson.exists()) {
            try {
                this.users.addAll(
                        this.mapper.readValue(
                                usersJson,
                                mapper.getTypeFactory()
                                        .constructCollectionType(List.class, User.class)
                        )
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sync() {
        try {
            this.mapper.writeValue(new File(DataConstants.JSON_USERS_FILE), this.users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String encryptedPassword) {
        return this.users.stream()
                .filter(user -> user.username().equals(username) && user.password().equals(encryptedPassword))
                .findFirst();
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return this.users.stream()
                .filter(user -> user.uuid().equals(uuid))
                .findFirst();
    }

    @Override
    public void save(User user) {
        if (Objects.isNull(user.uuid())) {
            if (this.users.stream().anyMatch(currentUser -> currentUser.username().equals(user.username()))) {
                throw new UserAlreadyExistsException("User with username " + user.username() + " already exists");
            }

            this.users.add(
                    User.builder(user)
                            .uuid(UUID.randomUUID())
                            .balance(new Balance(BigDecimal.ZERO, Currency.EURO))
                            .build()
            );
        } else {
            int index = this.users.indexOf(user);
            if (index < 0) {
                throw new NoSuchElementException("User with uuid " + user.uuid() + " not found");
            }
            this.users.set(index, user);
        }
    }
}
