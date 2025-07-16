package com.link.EmployeesWebApp.repository;


import com.link.EmployeesWebApp.model.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return employeeFromRs(rs);
    }

    private static Employee employeeFromRs(ResultSet rs) throws SQLException {
        Employee employee = new Employee();

        employee.setId(rs.getInt("id"));

        String firstName = rs.getString("firstName");
        employee.setFirstName(firstName);
        employee.setLastName(rs.getString("lastName"));
        employee.setBirthDate(rs.getDate("birthDate"));
        employee.setCnp(rs.getString("cnp"));
     //   employee.setIdJob(rs.getInt("job_id"));
        employee.setSalary(rs.getFloat("salary"));
        employee.setAge(rs.getInt("age"));

        return employee;
    }

}
