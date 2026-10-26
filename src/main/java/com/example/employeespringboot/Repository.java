package com.example.employeespringboot;

import java.util.List;

public interface Repository<T, ID> {
    T add(T entity);
    T update(T entity);
    boolean delete(ID id);
    T findById(ID id);
    List<T> findAll();
}

