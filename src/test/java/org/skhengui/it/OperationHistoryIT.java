package org.skhengui.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skhengui.dao.OperationDao;
import org.skhengui.dao.impl.OperationDaoImpl;
import org.skhengui.models.Balance;
import org.skhengui.models.Currency;
import org.skhengui.models.Operation;
import org.skhengui.models.OperationType;
import org.skhengui.services.OperationService;
import org.skhengui.services.impl.OperationServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Operation History IT")
public class OperationHistoryIT {
    private OperationService operationService;

    private static final UUID USER_UUID = UUID.randomUUID();

    @BeforeEach
    void setup() {
        OperationDao operationDao = new OperationDaoImpl();
        operationService = new OperationServiceImpl(operationDao);
    }

    @Test
    @DisplayName("Should return empty list when there are no operations")
    void shouldReturnEmptyListWhenNoOperations() {
        List<Operation> actualOperations = operationService.getOperationsForUserWithUuid(USER_UUID);
        assertThat(actualOperations).isEmpty();
    }

    @Test
    @DisplayName("Should return operation's history for user")
    void shouldReturnOperationsHistoryForUser() {
        List<Operation> operations = List.of(
                getOperation(OperationType.DEPOSIT, BigDecimal.valueOf(6556D)),
                getOperation(OperationType.DEPOSIT, BigDecimal.valueOf(1561D)),
                getOperation(OperationType.WITHDRAWAL, BigDecimal.valueOf(56D)),
                getOperation(OperationType.DEPOSIT, BigDecimal.valueOf(65D)),
                getOperation(OperationType.DEPOSIT, BigDecimal.valueOf(1655)),
                getOperation(OperationType.WITHDRAWAL, BigDecimal.valueOf(3D))
        );
        operations.forEach(operationService::save);

        List<Operation> actualOperations = operationService.getOperationsForUserWithUuid(USER_UUID);
        assertThat(actualOperations)
                .map(operation -> Operation.builder(operation).uuid(null).build())
                .containsExactlyInAnyOrderElementsOf(operations);
    }


    private Operation getOperation(OperationType type, BigDecimal amount) {
        return Operation.builder()
                .userUUID(USER_UUID)
                .type(type)
                .amount(amount)
                .updatedBalance(new Balance(BigDecimal.valueOf(new Random().nextDouble()), Currency.EURO))
                .createdAt(Instant.now())
                .build();
    }
}
