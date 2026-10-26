package com.example.employeespringboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
@Repository 
public class EmployeeRepositoryInMemoryImpl implements EmployeeRepository {

    private final List<Employee> employees = new ArrayList<>();
    private final Map<Integer, List<Employee>> employeesByDepartment = new HashMap<>();
    private final Map<Integer, List<Employee>> employeesByManager = new HashMap<>();

    @Override
    public Employee add(Employee employee) {
        for (Employee existingEmployee : employees) {
            if (existingEmployee.getId() == employee.getId()) {
                throw new DuplicateEmployeeException(
                        "Employee with ID " + employee.getId() + " already exists"
                );
            }
        }

        employees.add(employee);
        addToDepartmentIndex(employee);
        addToManagerIndex(employee);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            Employee oldEmployee = employees.get(i);

            if (oldEmployee.getId() == employee.getId()) {
                removeFromDepartmentIndex(oldEmployee);
                removeFromManagerIndex(oldEmployee);
                employees.set(i, employee);
                addToDepartmentIndex(employee);
                addToManagerIndex(employee);
                return employee;
            }
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        Employee employee = findById(id);

        if (employee == null) {
            return false;
        }

        employees.remove(employee);
        removeFromDepartmentIndex(employee);
        removeFromManagerIndex(employee);
        return true;
    }

    @Override
    public Employee findById(Integer id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees);
    }

    @Override
    public List<Employee> findByDepartment(int departmentId) {
        List<Employee> result = employeesByDepartment.get(departmentId);
        return result == null ? new ArrayList<>() : new ArrayList<>(result);
    }

    @Override
    public List<Employee> findByManager(int managerId) {
        List<Employee> result = employeesByManager.get(managerId);
        return result == null ? new ArrayList<>() : new ArrayList<>(result);
    }

    private void addToDepartmentIndex(Employee employee) {
        employeesByDepartment
                .computeIfAbsent(employee.getDepartmentId(), key -> new ArrayList<>())
                .add(employee);
    }

    private void addToManagerIndex(Employee employee) {
        if (employee.getManagerId() != null) {
            employeesByManager
                    .computeIfAbsent(employee.getManagerId(), key -> new ArrayList<>())
                    .add(employee);
        }
    }

    private void removeFromDepartmentIndex(Employee employee) {
        List<Employee> list = employeesByDepartment.get(employee.getDepartmentId());

        if (list != null) {
            list.remove(employee);
            if (list.isEmpty()) {
                employeesByDepartment.remove(employee.getDepartmentId());
            }
        }
    }

    private void removeFromManagerIndex(Employee employee) {
        if (employee.getManagerId() == null) {
            return;
        }

        List<Employee> list = employeesByManager.get(employee.getManagerId());

        if (list != null) {
            list.remove(employee);
            if (list.isEmpty()) {
                employeesByManager.remove(employee.getManagerId());
            }
        }
    }
}

