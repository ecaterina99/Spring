package com.link.EmployeesWebApp.repository;

import com.link.EmployeesWebApp.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepositoryCrud extends CrudRepository<Employee, Integer> {
}
