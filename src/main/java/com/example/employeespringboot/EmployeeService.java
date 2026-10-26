
package com.example.employeespringboot;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

   private final EmployeeRepository repository;

    public EmployeeService(RepositoryFactory repositoryFactory) {
    this.repository = repositoryFactory.createRepository();
}

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Employee getEmployee(int id) {
        Employee employee = repository.findById(id);

        if (employee == null) {
            throw new EmployeeNotFoundException(
                    "Employee with ID " + id + " not found"
            );
        }

        return employee;
    }

    public Employee addEmployee(Employee employee) {
        return repository.add(employee);
    }

    public Employee updateEmployee(Employee employee) {
        return repository.update(employee);
    }

    public boolean deleteEmployee(int id) {
        return repository.delete(id);
    }
}
