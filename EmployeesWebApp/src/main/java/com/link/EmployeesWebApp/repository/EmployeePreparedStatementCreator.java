package com.link.EmployeesWebApp.repository;
import com.link.EmployeesWebApp.model.Employee;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeePreparedStatementCreator implements PreparedStatementCreator {

    @Getter
    @Setter
    private String query;

    @Getter
    @Setter
    private Employee employee;

    @Override
    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, employee.getFirstName());
        ps.setString(2, employee.getLastName());
        ps.setFloat(3, employee.getSalary());
        ps.setInt(4, employee.getAge());
        return ps;
    }

}
