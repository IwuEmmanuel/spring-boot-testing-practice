package com.TestingTutorials.springboottesting.services;

import com.TestingTutorials.springboottesting.Data.Entities.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveNewEmployee(Employee employee);

    List<Employee> getAllEmloyees();

    Optional<Employee> getEmployeeByID(Long employeeId);
}
