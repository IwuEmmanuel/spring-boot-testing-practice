package com.TestingTutorials.springboottesting.Data.Repositories;

import com.TestingTutorials.springboottesting.Data.Entities.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryITest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
    }

    //Junit test for save employee operation
    @Test
    @DisplayName("Junit test for save employee operation ")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee () {

        //given Employee Object
        Employee employee = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        //when
        Employee savedEmployee = employeeRepository.save(employee);

        //then
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getEmployeeId()).isGreaterThan(0);
    }

    //Junit Test for ...
    @Test
    @DisplayName("Junit test for findAll employee operation")
    public void givenAListOFEmployees_whenFindAll_thenReturnEmployeeList(){

        // given - preconditions or setup
        Employee employee1 = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Angel")
                .lastName("Sam-Aliyu")
                .email("angel@gmail.com")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // when - action or the behaviour that we are going to test

        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output

        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isGreaterThan(1);
        Assertions.assertThat(employeeList.size()).isEqualTo(2);

    }

    //Junit Test for get employee by id operation
    @Test
    public void givenEmployeeId_whenFindById_thenReturnSavedEmployee(){

        // given - preconditions or setup
        Employee employee1 = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going to test
        Employee employee = employeeRepository.findById(employee1.getEmployeeId()).get();

        // then - verify the output
        Assertions.assertThat(employee).isNotNull();

    }

    //Junit Test for get employee by email operation
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnSavedEmployee(){

        // given - preconditions or setup
        Employee employee1 = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going to test
        Employee employee = employeeRepository.findByEmail(employee1.getEmail()).get();

        // then - verify the output
        Assertions.assertThat(employee).isNotNull();

    }

    //Junit Test for update employee by Email operation
    @Test
    public void givenEmployeeFirstName_whenFindByFirstName_thenReturnSavedEmployee(){

        // given - preconditions or setup
        Employee employee1 = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going to test
        Employee employee = employeeRepository.findByFirstName(employee1.getFirstName()).get();

        // then - verify the output
        Assertions.assertThat(employee).isNotNull();

    }

    //Junit Test for get employee by email operation
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        // given - preconditions or setup
        Employee employee1 = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByEmail(employee1.getEmail()).get(); // You can also use ID
        savedEmployee.setEmail("newEmail@gmail.com");
        savedEmployee.setFirstName("Coolio");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output
        Assertions.assertThat(updatedEmployee).isNotNull();
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("newEmail@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Coolio");

    }

    //Junit Test for custom query using JPQL with index params
    @Test
    @DisplayName("Junit Test for custom query using JPQL with index params")
    public void givenFirstNameAndLastName_whenFindByFirstNameAndLastName_thenReturnEmployeeObject(){

        // given - preconditions or setup
        Employee employee1 = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastName(employee1.getFirstName(), employee1.getLastName());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();

    }

    //Junit Test for custom query using JPQL with Named params
    @Test
    @DisplayName("Junit Test for custom query using JPQL with Named params")
    public void givenFirstNameAndEmail_whenFindByFirstNameAndEmail_thenReturnEmployeeObject(){

        // given - preconditions or setup
        Employee employee1 = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByFirstNameAndEmail(employee1.getFirstName(), employee1.getEmail());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();

    }

    //Junit Test for custom query using Native SQL with index params
    @Test
    @DisplayName("Junit Test for custom query using Native SQL with index params")
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){

        // given - preconditions or setup
        Employee employee1 = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee1.getFirstName(), employee1.getLastName());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();

    }

    //Junit Test for custom query using NativeSQL with Named params
    @Test
    @DisplayName("Junit Test for custom query using NamedSQL with Named params")
    public void givenFirstNameAndLastName_whenFindByNativeSQLWithNamedParams_thenReturnEmployeeObject(){

        // given - preconditions or setup
        Employee employee1 = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();

        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLWithNamedParams(employee1.getFirstName(), employee1.getLastName());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();

    }


}