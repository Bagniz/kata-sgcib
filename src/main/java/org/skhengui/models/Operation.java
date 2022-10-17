package org.skhengui.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Operation(
        UUID uuid,
        UUID userUUID,
        OperationType type,
        BigDecimal amount,
        Balance updatedBalance,
        Instant createdAt
) implements Serializable {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Operation operation) {
        return new Builder()
                .uuid(operation.uuid)
                .userUUID(operation.userUUID)
                .type(operation.type)
                .amount(operation.amount)
                .updatedBalance(operation.updatedBalance)
                .createdAt(operation.createdAt);
    }

    @Override
    public String toString() {
        return "Operation of type " + this.type() +
                ", at " + this.createdAt() +
                ", the amount of " + this.amount() +
                ", updated updatedBalance " + this.updatedBalance().amount() + " " + this.updatedBalance().currency();
    }

    public static final class Builder {
        private UUID uuid;
        private UUID userUUID;
        private OperationType type;
        private BigDecimal amount;
        private Balance updatedBalance;
        private Instant createdAt;

        private Builder() {
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder userUUID(UUID userUUID) {
            this.userUUID = userUUID;
            return this;
        }

        public Builder type(OperationType type) {
            this.type = type;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder updatedBalance(Balance updatedBalance) {
            this.updatedBalance = updatedBalance;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Operation build() {
            return new Operation(uuid, userUUID, type, amount, updatedBalance, createdAt);
        }
    }
}
