package org.skhengui.services.impl;

import org.skhengui.dao.OperationDao;
import org.skhengui.models.Operation;
import org.skhengui.services.OperationService;

import java.util.List;
import java.util.UUID;

public class OperationServiceImpl implements OperationService {
    private final OperationDao operationDao;

    public OperationServiceImpl(OperationDao operationDao) {
        this.operationDao = operationDao;
    }

    @Override
    public List<Operation> getOperationsForUserWithUuid(UUID uuid) {
        return this.operationDao.getOperationsForUserWithUuid(uuid);
    }

    @Override
    public void save(Operation operation) {
        this.operationDao.save(operation);
    }
}
