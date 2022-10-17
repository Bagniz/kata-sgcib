package org.skhengui.services;

import org.skhengui.models.Operation;

import java.util.List;
import java.util.UUID;

public interface OperationService {
    List<Operation> getOperationsForUserWithUuid(UUID uuid);
    void save(Operation operation);
}
