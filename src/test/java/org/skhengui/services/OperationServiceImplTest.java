package org.skhengui.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skhengui.dao.OperationDao;
import org.skhengui.dao.impl.OperationDaoImpl;
import org.skhengui.models.Balance;
import org.skhengui.models.Currency;
import org.skhengui.models.Operation;
import org.skhengui.models.OperationType;
import org.skhengui.services.impl.OperationServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Operation Service Impl Test")
public class OperationServiceImplTest {
    private final OperationDao operationDao = mock(OperationDaoImpl.class);
    private final OperationService operationService = new OperationServiceImpl(operationDao);

    private static final UUID USER_UUID = UUID.randomUUID();

    @Nested
    @DisplayName("Get Operations For User")
    class GetOperationForUser {
        @Test
        @DisplayName("Should return empty list when there are no operations")
        void shouldReturnEmptyListWhenNoOperations() {

            when(operationDao.getOperationsForUserWithUuid(any())).thenReturn(Collections.emptyList());
            List<Operation> actualOperations = operationService.getOperationsForUserWithUuid(USER_UUID);
            assertAll(() -> {
                verify(operationDao, times(1)).getOperationsForUserWithUuid(USER_UUID);
                assertThat(actualOperations).isEmpty();
            });
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
            when(operationDao.getOperationsForUserWithUuid(any())).thenReturn(operations);
            List<Operation> actualOperations = operationService.getOperationsForUserWithUuid(USER_UUID);
            assertAll(() -> {
                verify(operationDao, times(1)).getOperationsForUserWithUuid(USER_UUID);
                assertThat(actualOperations).containsExactlyInAnyOrderElementsOf(operations);
            });
        }


    }

    @Nested
    @DisplayName("Save")
    class Save {
        @Test
        @DisplayName("Should call dao save method")
        void verifyCallOperationDaoSave() {
            Operation operation = getOperation(OperationType.DEPOSIT, BigDecimal.valueOf(5452));
            operationService.save(operation);
            verify(operationDao, times(1)).save(operation);
        }
    }

    private Operation getOperation(OperationType type, BigDecimal amount) {
        return Operation.builder()
                .uuid(UUID.randomUUID())
                .userUUID(UUID.randomUUID())
                .type(type)
                .amount(amount)
                .updatedBalance(new Balance(BigDecimal.valueOf(new Random().nextDouble()), Currency.EURO))
                .createdAt(Instant.now())
                .build();
    }
}
