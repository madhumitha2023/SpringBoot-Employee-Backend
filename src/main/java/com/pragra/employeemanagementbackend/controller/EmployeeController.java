package com.pragra.employeemanagementbackend.controller;

import com.pragra.employeemanagementbackend.exception.ResourceNotFound;
import com.pragra.employeemanagementbackend.model.Employee;
import com.pragra.employeemanagementbackend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
    @Autowired
    private final EmployeeRepository employeeRepository;

    //get all employees rest api
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    //create employee rest api
    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    //get employee by id
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Employee not exist with ID:"+ id));

        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    //update employee rest api
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee){
        Employee opEmployee = employeeRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Employee Not exist with Id: "+ id));
        if(null!=employee.getFirstName() && null!=employee.getEmailId()){
            opEmployee.setFirstName(employee.getFirstName());
            opEmployee.setLastName(employee.getLastName());
            opEmployee.setEmailId(employee.getEmailId());
        }
        Employee updatedEmployee = employeeRepository.save(opEmployee);
        return ResponseEntity.ok(updatedEmployee);
    }

    //delete employee rest api
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteEmployee(@PathVariable Long id){
        Employee opEmployee = employeeRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Employee Not exist with Id: "+ id));

        employeeRepository.delete(opEmployee);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
