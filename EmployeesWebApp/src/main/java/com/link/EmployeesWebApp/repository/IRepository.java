package com.link.EmployeesWebApp.repository;

import java.util.List;

public interface IRepository<T> {

    T add(T employee);

    T update(T employee, int id);

    boolean delete(int id);

    List<T> findAll();

    T find(int id);

}
