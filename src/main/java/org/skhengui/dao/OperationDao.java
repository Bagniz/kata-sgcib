package org.skhengui.dao;

import org.skhengui.models.Operation;

import java.util.List;
import java.util.UUID;

public interface OperationDao {
    void init();
    List<Operation> getOperationsForUserWithUuid(UUID uuid);
    void save(Operation operation);
}
