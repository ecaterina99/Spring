package com.link.hello.repository;

import com.link.hello.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeRepositoryJdbc implements IRepository<Employee> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final EmployeeRowMapper employeeRowMapper = new EmployeeRowMapper();

    public Employee add(Employee employee) {
        String query = "INSERT INTO employees " +
                "(id, firstName, lastName, salary, age) " +
                "VALUES " +
                "(0, ?, ?, ?, ?)";
        EmployeePreparedStatementCreator epsc = new EmployeePreparedStatementCreator();
        epsc.setQuery(query);
        epsc.setEmployee(employee);
        try {
            epsc.createPreparedStatement(jdbcTemplate.getDataSource().getConnection());
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(epsc, keyHolder);
        int lastInsertedId = keyHolder.getKey().intValue();
        return this.find(lastInsertedId);
    }

    public Employee update(Employee employee, int id) {
        String query = " UPDATE employees SET " +
                " firstName = ?, lastName = ?, " +
                " salary = ?, age = ?" +
                " WHERE id = ? " +
                " LIMIT 1 ";
        jdbcTemplate.update(query,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getSalary(),
                employee.getAge(),
                id
        );
        return this.find(id);
    }

    public List<Employee> findAll() {
        String query = "SELECT * FROM employees";
        return jdbcTemplate.query(query, employeeRowMapper);
    }

    public Employee find(int id) {
        String query = "SELECT * FROM employees WHERE id = ?";
        return jdbcTemplate.queryForObject(query, employeeRowMapper, id);
    }

    public boolean delete(int id) {
        String query = "DELETE FROM employees WHERE id = ? LIMIT 1";
        return jdbcTemplate.update(query, id) > 0;
    }
}
