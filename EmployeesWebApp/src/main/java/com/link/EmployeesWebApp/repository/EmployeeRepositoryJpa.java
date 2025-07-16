package com.link.EmployeesWebApp.repository;

import com.link.EmployeesWebApp.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepositoryJpa extends JpaRepository<Employee, Integer> {
    // find by last name
    // SELECT * FROM employees WHERE last_name = "valoare"
    List<Employee> findByLastName(String lastName);

    // SELECT * FROM employees WHERE last_name LIKE "%valoare%"
    List<Employee> findByLastNameLike(String lastName);

    // SELECT * FROM employees WHERE last_name LIKE "%valoare%"
    // TODO: Fix it
    List<Employee> findFirstNameDistinctByLastNameContains(String lastName);

    // SELECT * FROM employees WHERE last_name LIKE "%nume%" AND first_name LIKE "%prenume%"
    List<Employee> findByLastNameContainsAndFirstNameContains(String lastName, String firstName);

    // SELECT * FROM employees WHERE age BETWEEN 30 AND 40
    Set<Employee> findByAgeIsBetween(int age1, int age2);

    // select COUNT(*) from employees where last_name like "%nume%"
    int countByLastNameContains(String lastName);

    // SELECT * FROM employees WHERE last_name = "%nume%" LIMIT 1
    Employee findFirstByLastNameContains(String lastName);

    // SELECT * FROM employees WHERE last_name = "%nume%" ORDER BY age DESC, first_name DESC LIMIT 1
    Employee findFirstByLastNameContainsOrderByAgeDescFirstNameDesc(String lastname);

    // DELETE FROM employees WHERE age = value
    @Transactional
    int deleteByAge(int age);

    List<Employee> findByAgeIsGreaterThanEqual(int age);

    @Query("SELECT e FROM employees e WHERE e.age >= :age")
    List<Employee> findOldEmployees(@Param("age") int age);

    @Query("SELECT e FROM employees e WHERE e.age >= :age AND e.salary >= :minSalary AND e.mainJob != null") // JPL (Java Persistence Language)
    List<Employee> findOldRichEmployees(@Param("age") int age, float minSalary);

    // TODO: native query (SQL)
    @Query(value = "SELECT * FROM employees e WHERE e.age >= :age AND e.salary >= :minSalary AND e.job_id != 0", nativeQuery = true)
    List<Employee> findOldRichEmployeesNative(@Param("age") int age, float minSalary);

    @Query("SELECT e FROM employees e JOIN e.mainJob")
    List<Employee> findAllEmployeesAndMainJob();

}
