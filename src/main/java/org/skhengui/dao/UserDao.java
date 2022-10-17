package org.skhengui.dao;

import org.skhengui.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    void init();
    Optional<User> findByUsernameAndPassword(String username, String encryptedPassword);
    Optional<User> findByUuid(UUID uuid);
    void save(User user);
}
