package org.skhengui.services.impl;

import org.skhengui.dao.UserDao;
import org.skhengui.models.User;
import org.skhengui.services.UserService;

import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> login(String username, String encryptedPassword) {
        return this.userDao.findByUsernameAndPassword(username, encryptedPassword);
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return this.userDao.findByUuid(uuid);
    }

    @Override
    public void save(User user) {
        this.userDao.save(user);
    }
}
