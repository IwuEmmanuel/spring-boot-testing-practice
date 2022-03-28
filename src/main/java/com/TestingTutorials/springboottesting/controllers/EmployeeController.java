package com.TestingTutorials.springboottesting.controllers;


import com.TestingTutorials.springboottesting.Data.Entities.Employee;
import com.TestingTutorials.springboottesting.exceptions.ResourceNotFoundException;
import com.TestingTutorials.springboottesting.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveNewEmployee(employee);
    }
}
