package com.example.employeespringboot;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryMongoImpl implements EmployeeRepository {

    private final MongoCollection<Document> collection;

    public EmployeeRepositoryMongoImpl() {
        collection = MongoConnection
                .getDatabase()
                .getCollection("employee");
    }

    @Override
    public Employee add(Employee employee) {
        if (collection.find(Filters.eq("id", employee.getId())).first() != null) {
            throw new DuplicateEmployeeException(
                    "Employee with ID " + employee.getId() + " already exists");
        }

        Document document = new Document("id", employee.getId())
                .append("name", employee.getName())
                .append("email", employee.getEmail())
                .append("salary", employee.getSalary())
                .append("departmentId", employee.getDepartmentId())
                .append("managerId", employee.getManagerId());

        collection.insertOne(document);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        if (findById(employee.getId()) == null) {
            return null;
        }

        Document document = new Document("id", employee.getId())
                .append("name", employee.getName())
                .append("email", employee.getEmail())
                .append("salary", employee.getSalary())
                .append("departmentId", employee.getDepartmentId())
                .append("managerId", employee.getManagerId());

        collection.replaceOne(Filters.eq("id", employee.getId()), document);
        return employee;
    }

    @Override
    public boolean delete(Integer id) {
        return collection.deleteOne(Filters.eq("id", id)).getDeletedCount() > 0;
    }

    @Override
    public Employee findById(Integer id) {
        Document document = collection.find(Filters.eq("id", id)).first();
        return document == null ? null : convertToEmployee(document);
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();

        for (Document document : collection.find()) {
            employees.add(convertToEmployee(document));
        }

        return employees;
    }

    @Override
    public List<Employee> findByDepartment(int departmentId) {
        List<Employee> employees = new ArrayList<>();

        for (Document document :
                collection.find(Filters.eq("departmentId", departmentId))) {
            employees.add(convertToEmployee(document));
        }

        return employees;
    }

    @Override
    public List<Employee> findByManager(int managerId) {
        List<Employee> employees = new ArrayList<>();

        for (Document document :
                collection.find(Filters.eq("managerId", managerId))) {
            employees.add(convertToEmployee(document));
        }

        return employees;
    }

    private Employee convertToEmployee(Document document) {
        Object managerValue = document.get("managerId");

        Integer managerId = managerValue == null
                ? null
                : ((Number) managerValue).intValue();

        return new Employee(
                document.getInteger("id"),
                document.getString("name"),
                document.getString("email"),
                document.getDouble("salary"),
                document.getInteger("departmentId"),
                managerId
        );
    }
}
