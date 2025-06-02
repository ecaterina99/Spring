package com.link.hello.repository;

import com.link.hello.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {

    @Autowired
    private DataSource dataSource;


    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) { // intrare noua din tabela
                Employee employee = employeeFromRs(rs);
                employees.add(employee);
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("SQL problem: " + e.getMessage());
        }
        return employees;
    }

    private static Employee employeeFromRs(ResultSet rs) throws SQLException {
        Employee employee = new Employee();

        employee.setId(rs.getInt("id"));
        String firstName = rs.getString("firstName");
        employee.setFirstName(firstName);
        employee.setLastName(rs.getString("lastName"));
        employee.setCnp(rs.getString("cnp"));
        employee.setBirthDate(rs.getDate("birthDate"));
        employee.setIdJob(rs.getInt("job_id"));
        employee.setSalary(rs.getFloat("salary"));
        employee.setAge(rs.getInt("age"));

        return employee;
    }
    public Employee find(int id) {
        String query = "SELECT * FROM employees WHERE id = ?";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return employeeFromRs(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
