package com.link.EmployeesWebApp.repository;

import com.link.EmployeesWebApp.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    public Employee add(Employee employee) {
        String query = "INSERT INTO employees " +
                "(id, firstName, lastName, salary, age) " +
                "VALUES " +
                "(0, ?, ?, ?, ?)";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setFloat(3, employee.getSalary());
            ps.setInt(4, employee.getAge());
            int numInserted = ps.executeUpdate();
            if (numInserted > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int last_inserted_id = rs.getInt(1);
                return this.find(last_inserted_id);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Employee update(Employee employee, int id) {
        StringBuilder sb = new StringBuilder("UPDATE employees SET ");
        Map<Integer, String> parameterIndex = new HashMap<>();
        int noParameters = 1;
        if (employee.getFirstName() != null) {
            sb.append(" firstName = ?, ");
            parameterIndex.put(noParameters++, employee.getFirstName());
        }
        if (employee.getLastName() != null) {
            sb.append(" lastName = ?, ");
            parameterIndex.put(noParameters++, employee.getLastName());
        }
        sb.append(" id=id WHERE id = ? LIMIT 1");
        String query = sb.toString();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            Set<Map.Entry<Integer, String>> entries = parameterIndex.entrySet();
            for (Map.Entry<Integer, String> entry : entries) {
                ps.setString(entry.getKey(), entry.getValue());
            }
            ps.setInt(noParameters, id);
            int numUpdated = ps.executeUpdate();
            if (numUpdated > 0) {
                return this.find(id);
            }
            Employee employeeInRepository = this.find(id);
            if (employeeInRepository == null) {
                // nu s-a facut update pentru ca nu exista id-ul ala
                return null;
            }
            // nu s-a facut update-ul din considerente de SQL; ii dau ce am acum in bd
            return employeeInRepository;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(int id) {
        String query = "DELETE FROM employees WHERE id = ? LIMIT 1";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            int numDeleted = ps.executeUpdate();
            return numDeleted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Employee employeeFromRs(ResultSet rs) throws SQLException {
        Employee employee = new Employee();

        employee.setId(rs.getInt("id"));
        employee.setBirthDate(rs.getDate("birthDate"));
        employee.setCnp(rs.getString("cnp"));
        String firstName = rs.getString("firstName");
        employee.setFirstName(firstName);
     // employee.setIdJob(rs.getInt("job_id"));
        employee.setLastName(rs.getString("lastName"));
        employee.setSalary(rs.getFloat("salary"));
        employee.setAge(rs.getInt("age"));
        return employee;
    }

}
