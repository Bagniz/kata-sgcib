package org.skhengui.utils;

import java.util.UUID;

public class UserThreadLocal {
    private static final ThreadLocal<UUID> CURRENT_USER_UUID = new ThreadLocal<>();

    public UUID getCurrentUserUuid() {
        return CURRENT_USER_UUID.get();
    }

    public void setCurrentUserUuid(UUID userUUID) {
        CURRENT_USER_UUID.set(userUUID);
    }
}
