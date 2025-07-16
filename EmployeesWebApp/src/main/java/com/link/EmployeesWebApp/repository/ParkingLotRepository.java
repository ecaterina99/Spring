package com.link.EmployeesWebApp.repository;

import com.link.EmployeesWebApp.model.Employee;
import com.link.EmployeesWebApp.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {

    @Query(value = "SELECT *, IF(p.employee_id, TRUE, FALSE) AS busy FROM parking_lots p", nativeQuery = true)
    List<ParkingLot> allLotsWithStatus();

    @Query(value = "SELECT * FROM employees e WHERE e.age >= :age AND e.salary >= :minSalary AND e.id_job != 0", nativeQuery = true)
    List<Employee> findOldRichEmployeesNative(@Param("age") int age, float minSalary);

}
