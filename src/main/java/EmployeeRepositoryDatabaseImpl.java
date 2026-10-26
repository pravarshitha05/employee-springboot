package com.example.employeespringboot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryDatabaseImpl implements EmployeeRepository {

    @Override
    public Employee add(Employee employee) {
        String sql = """
                INSERT INTO employee
                (id, name, email, salary, department_id, manager_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        if (findById(employee.getId()) != null) {
            throw new DuplicateEmployeeException(
                    "Employee with ID " + employee.getId() + " already exists");
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employee.getId());
            statement.setString(2, employee.getName());
            statement.setString(3, employee.getEmail());
            statement.setDouble(4, employee.getSalary());
            statement.setInt(5, employee.getDepartmentId());

            if (employee.getManagerId() != null) {
                statement.setInt(6, employee.getManagerId());
            } else {
                statement.setNull(6, java.sql.Types.INTEGER);
            }

            statement.executeUpdate();
            return employee;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add employee", e);
        }
    }

    @Override
    public Employee update(Employee employee) {
        String sql = """
                UPDATE employee
                SET name = ?, email = ?, salary = ?,
                    department_id = ?, manager_id = ?
                WHERE id = ?
                """;

        if (findById(employee.getId()) == null) {
            return null;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setDouble(3, employee.getSalary());
            statement.setInt(4, employee.getDepartmentId());

            if (employee.getManagerId() != null) {
                statement.setInt(5, employee.getManagerId());
            } else {
                statement.setNull(5, java.sql.Types.INTEGER);
            }

            statement.setInt(6, employee.getId());
            statement.executeUpdate();
            return employee;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update employee", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM employee WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete employee", e);
        }
    }

    @Override
    public Employee findById(Integer id) {
        String sql = "SELECT * FROM employee WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertToEmployee(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find employee", e);
        }
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employee";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                employees.add(convertToEmployee(resultSet));
            }
            return employees;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find employees", e);
        }
    }

    @Override
    public List<Employee> findByDepartment(int departmentId) {
        String sql = "SELECT * FROM employee WHERE department_id = ?";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, departmentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    employees.add(convertToEmployee(resultSet));
                }
            }
            return employees;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find employees by department", e);
        }
    }

    @Override
    public List<Employee> findByManager(int managerId) {
        String sql = "SELECT * FROM employee WHERE manager_id = ?";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, managerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    employees.add(convertToEmployee(resultSet));
                }
            }
            return employees;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find employees by manager", e);
        }
    }

    private Employee convertToEmployee(ResultSet resultSet)
            throws SQLException {

        int managerId = resultSet.getInt("manager_id");
        Integer manager = resultSet.wasNull() ? null : managerId;

        return new Employee(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getDouble("salary"),
                resultSet.getInt("department_id"),
                manager
        );
    }
}

