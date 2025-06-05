package com.link.hello.repository;

import com.link.hello.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepositoryCrud extends CrudRepository<Employee, Integer> {
}
