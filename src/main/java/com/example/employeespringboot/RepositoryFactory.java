package com.example.employeespringboot;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RepositoryFactory {

    private final ApplicationContext context;

    public RepositoryFactory(ApplicationContext context) {
        this.context = context;
    }

    public EmployeeRepository createRepository() {
        String storageType = context.getEnvironment()
                .getProperty("storage.type", "IN_MEMORY");

        if ("DATABASE".equalsIgnoreCase(storageType)) {
            return context.getBean(EmployeeRepositoryHibernateImpl.class);
        }

        if ("IN_MEMORY".equalsIgnoreCase(storageType)) {
            return context.getBean(EmployeeRepositoryInMemoryImpl.class);
        }

        if ("MONGO".equalsIgnoreCase(storageType)) {
            return context.getBean(EmployeeRepositoryMongoImpl.class);
        }

        throw new IllegalArgumentException(
                "Invalid storage.type: " + storageType
        );
    }
}
