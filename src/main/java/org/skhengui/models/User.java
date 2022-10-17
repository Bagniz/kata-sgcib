package org.skhengui.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record User (
        UUID uuid,
        String firstName,
        String lastName,
        String username,
        String password,
        Balance balance
) implements Serializable {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(User user) {
        return new Builder()
                .uuid(user.uuid)
                .firstName(user.firstName)
                .lastName(user.lastName)
                .username(user.username)
                .password(user.password)
                .balance(user.balance);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uuid, user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    public static final class Builder {
        private UUID uuid;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private Balance balance;

        private Builder() {
        }
        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder firstName(String firstname) {
            this.firstName = firstname;
            return this;
        }

        public Builder lastName(String lastname) {
            this.lastName = lastname;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder balance(Balance balance) {
            this.balance = balance;
            return this;
        }

        public User build() {
            return new User(uuid, firstName, lastName, username, password, balance);
        }
    }
}
