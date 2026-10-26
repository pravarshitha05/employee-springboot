
package com.example.employeespringboot;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Sort;
@Repository 
public class EmployeeRepositoryHibernateImpl implements EmployeeRepository {

    private final EmployeeJpaRepository jpaRepository;

    public EmployeeRepositoryHibernateImpl(
            EmployeeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Employee add(Employee employee) {
        if (jpaRepository.existsById(employee.getId())) {
            throw new DuplicateEmployeeException(
                    "Employee with ID " + employee.getId() + " already exists"
            );
        }

        return jpaRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        if (!jpaRepository.existsById(employee.getId())) {
            throw new EmployeeNotFoundException(
                    "Employee with ID " + employee.getId() + " not found"
            );
        }

        return jpaRepository.save(employee);
    }

    @Override
    public boolean delete(Integer id) {
        if (!jpaRepository.existsById(id)) {
            return false;
        }

        jpaRepository.deleteById(id);
        return true;
    }

    @Override
    public Employee findById(Integer id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        return jpaRepository.findAll(Sort.by("id"));
    }

    @Override
    public List<Employee> findByDepartment(int departmentId) {
        return jpaRepository.findAll().stream()
                .filter(employee -> employee.getDepartmentId() == departmentId)
                .toList();
    }

    @Override
    public List<Employee> findByManager(int managerId) {
        return jpaRepository.findAll().stream()
                .filter(employee -> employee.getManagerId() != null
                        && employee.getManagerId() == managerId)
                .toList();
    }
}


