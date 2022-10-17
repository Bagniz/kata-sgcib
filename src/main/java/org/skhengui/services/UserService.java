package org.skhengui.services;

import org.skhengui.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> login(String username, String encryptedPassword);
    Optional<User> findByUuid(UUID uuid);
    void save(User user);
}
