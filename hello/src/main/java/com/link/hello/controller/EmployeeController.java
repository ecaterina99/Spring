package com.link.hello.controller;

import com.link.hello.dto.EmployeeDTO;
import com.link.hello.dto.ResponseDTO;
import com.link.hello.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Return all employees: GET /employee/
    @GetMapping("/")
    public List<EmployeeDTO> getEmployees() {
        return employeeService.findAll();
    }

    // Retrieve employee: GET /employee/{id}
    @GetMapping("/{id}")
    public ResponseDTO showEmployee(
            @PathVariable int id
    ) {
        ResponseDTO responseDTO = new ResponseDTO();

        EmployeeDTO employeeDTO = employeeService.find(id);
        responseDTO.setData(employeeDTO);

        responseDTO.setSuccess(employeeDTO != null);

        return responseDTO;
    }

    // Add new employee: POST /employee/
    @PostMapping("/")
    public ResponseEntity<Object> addEmployee(
            @RequestBody EmployeeDTO employeeDTO
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (employeeDTO.getAge() < 18 || employeeDTO.getAge() > 60) {
            return ResponseEntity.ok(new HashMap<>());
        }
        // TODO: add other conditions to validate input
        EmployeeDTO employeeDTOResponse = employeeService.addEmployee(employeeDTO);
        return ResponseEntity.ok(employeeDTOResponse != null ? employeeDTOResponse : new HashMap<>());
    }

    // Update employee: PUT /employee/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @RequestBody EmployeeDTO employeeDTO,
            @PathVariable int id
    ) {
        if (employeeDTO.getId() != 0) {
            employeeDTO.setId(0);
        }
        // TODO: Check with debugger why it doesn't provide OK
        EmployeeDTO employeeDTOResponse = employeeService.update(employeeDTO, id);
        if (employeeDTOResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(employeeDTOResponse);
    }

    // Delete employee: DELETE /employee/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(
            @PathVariable int id
    ) {
        boolean wasDeleted = employeeService.delete(id);
        if (wasDeleted) {
            return ResponseEntity.ok(new HashMap<>()); // empty JSON object confirms deletion
        }
        return ResponseEntity.badRequest().build();
    }

}
