package org.skhengui.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.skhengui.dao.OperationDao;
import org.skhengui.dao.SyncOnExit;
import org.skhengui.models.Operation;
import org.skhengui.utils.DataConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OperationDaoImpl implements OperationDao, SyncOnExit {
    private final List<Operation> operations;
    private final ObjectMapper mapper;

    public OperationDaoImpl() {
        operations = Collections.synchronizedList(new ArrayList<>());
        mapper = JsonMapper.builder().findAndAddModules().build();
    }

    @Override
    public void init() {
        File operationsJson = new File(DataConstants.JSON_OPERATIONS_FILE);
        if (operationsJson.exists()) {
            try {
                this.operations.addAll(
                        this.mapper.readValue(
                                operationsJson,
                                mapper.getTypeFactory()
                                        .constructCollectionType(List.class, Operation.class)
                        )
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sync() {
        try {
            this.mapper.writeValue(new File(DataConstants.JSON_OPERATIONS_FILE), this.operations);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Operation> getOperationsForUserWithUuid(UUID uuid) {
        return this.operations.stream()
                .filter(operation -> operation.userUUID().equals(uuid))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Operation operation) {
        this.operations.add(
                Operation.builder(operation)
                        .uuid(UUID.randomUUID())
                        .build()
        );
    }
}
