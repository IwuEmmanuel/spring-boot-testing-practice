package com.TestingTutorials.springboottesting.services.ServiceImpl;

import com.TestingTutorials.springboottesting.Data.Entities.Employee;
import com.TestingTutorials.springboottesting.Data.Repositories.EmployeeRepository;
import com.TestingTutorials.springboottesting.exceptions.ResourceNotFoundException;
import com.TestingTutorials.springboottesting.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

//    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl (EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveNewEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee already exists with given email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmloyees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeByID(Long employeeId) {

        Optional<Employee> savedEmployee = employeeRepository.findById(employeeId);

        return savedEmployee;
    }

    @Override
    public Employee UpdateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
