
package com.example.employeespringboot;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, Integer> {

    List<Employee> findByDepartment(int departmentId);

    List<Employee> findByManager(int managerId);
}
