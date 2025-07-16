package com.link.EmployeesWebApp.controller.api;


import com.link.EmployeesWebApp.dto.AllEmployeesDTO;
import com.link.EmployeesWebApp.dto.EmployeeDTO;
import com.link.EmployeesWebApp.dto.ResponseDTO;
import com.link.EmployeesWebApp.model.Employee;
import com.link.EmployeesWebApp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeApiController {

    @Autowired
    private EmployeeService employeeService;

    // Return all employees: GET /employee/
    @GetMapping("/")
    public AllEmployeesDTO showEmployees() {
        AllEmployeesDTO allEmployeesDTO = new AllEmployeesDTO();
        List<EmployeeDTO> employeeDTOs = employeeService.findAllAndMainJob();
        allEmployeesDTO.setEmployees(employeeDTOs);
        allEmployeesDTO.setCount(employeeService.count());
        return allEmployeesDTO;
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


    @GetMapping("/main")
    public List<EmployeeDTO> employeesWithMainJob() {
        return employeeService.findAllAndMainJob();
    }

    @GetMapping("/model")
    public List<Employee> showEmployeesModel() {
        return employeeService.findModelAllAndMainJob();
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
    public ResponseEntity<Object> updateEmployee(
            @RequestBody EmployeeDTO employeeDTO,
            @PathVariable int id
    ) {
        if (employeeDTO.getId() != 0) {
            employeeDTO.setId(0);
        }
        // TODO: Check with debugger why it doesn't provide OK
        EmployeeDTO employeeDTOResponse = employeeService.update(employeeDTO, id);
        if (employeeDTOResponse == null) {
//            return ResponseEntity.badRequest().build(); // 400 BadRequest
            return ResponseEntity.ok(new HashMap<>()); // 200 OK cu {}
        }
        return ResponseEntity.ok(employeeDTOResponse);
    }



    // Delete employee: DELETE /employee/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(
            @PathVariable int id
    ) {
        if (id < 0) return ResponseEntity.badRequest().build();
        boolean wasDeleted = employeeService.delete(id);
        if (wasDeleted) {
            return ResponseEntity.ok(new HashMap<>()); // empty JSON object confirms deletion
        }
        return ResponseEntity.badRequest().build();
    }

    // Delete employees with specific age: DELETE /employee/age/{age}
    @DeleteMapping("/age/{age}")
    public Integer deleteEmployeesByAge(
            @PathVariable int age
    ) {
        return employeeService.deleteByAge(age);
    }


    // Return employees with a specific last name: GET /employee/search
    @GetMapping("/search")
    public ResponseEntity search(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) Integer startAge,
            @RequestParam(required = false) Integer endAge
    ) {
        // startAge should be provided with endAge (pair)
        if ((startAge != null && endAge == null) || (endAge != null && startAge == null)) {
            return ResponseEntity.badRequest().build();
        }

        // first name cannot come without last name
        if (startAge == null && lastName == null) {
            return ResponseEntity.badRequest().build();
        }
        // query with age between
        if (startAge != null) { // && endAge != null
            return ResponseEntity.ok(employeeService.search(startAge, endAge));
       }

        // query with last name
        if (firstName == null) {
            // i have only last name
            return ResponseEntity.ok(employeeService.search(lastName));
        }
        // query with last name and first name
       return ResponseEntity.ok(employeeService.search(lastName, firstName));
    }

    @GetMapping("/count")
    public Integer countByLastName(
            @RequestParam String lastName
    ) {
        return employeeService.countByLastName(lastName);
    }

   @GetMapping("/find-one")
    public EmployeeDTO findOne(
            @RequestParam String lastName
    ) {
        return employeeService.find(lastName);
    }

    @GetMapping("/old")
    public List<EmployeeDTO> oldEmployees(
            @RequestParam int age
    ) {
        return employeeService.findOldEmployees(age);
    }

    // Select employees with age between: GET /employee/age-between/{start}/{end}
    @GetMapping("/age-between/{start}/{end}")
    public List<EmployeeDTO> allBetweenAges(
            @PathVariable(name = "start") int startAge,
            @PathVariable(name = "end") int endAge
    ) {
        return employeeService.search(startAge, endAge);
    }

}
