package com.TestingTutorials.springboottesting.services.ServiceImpl;

import com.TestingTutorials.springboottesting.Data.Entities.Employee;
import com.TestingTutorials.springboottesting.Data.Repositories.EmployeeRepository;
import com.TestingTutorials.springboottesting.exceptions.ResourceNotFoundException;
import com.TestingTutorials.springboottesting.services.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


/**
 * Use @DataJpaTest and TestEntityManager(persist and flush) for testing service layers if you need to persist
 * the data in the database
 * because you haven't tested your repository layer or your entities extend a baseEntity class making
 * it impossible for mockito to successfully mock the superclass methods like ID generation.
 *
 * Note that injection of mocks don't work for service interfaces since they usually have no constructor. Rather
 * mock the service implementations.
 *
 * Note @Mock does not mock the repository completely and needs @DataJpaTest to successfully assess the mocked objects
 * from the repository.
 * Use @DataJpaTest with @Mock ..... And Use Mockito.mock(repository.class) alone. Both would behave as expected. I have to confirm why!!!!!
 * **/

//@DataJpaTest
class EmployeeServiceImplTest {

//    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    private Employee employee1;



//    @InjectMocks
//    private EmployeeServiceImpl employeeService;

//    @Autowired
//    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);

        employee1 = Employee.builder()
                .employeeId(1L)
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("starcy008@gmail.com")
                .build();
    }

    @Test
    @DisplayName("Junit test case for save employee method implementation")
    void givenEmployeeObject_whenSaveNewEmployee_thenReturnSavedEmployee() {

        //Given

//        testEntityManager.persistAndFlush(employee1);

        BDDMockito.given(employeeRepository.findByEmail(employee1.getEmail())).willReturn(Optional.empty());

        BDDMockito.given(employeeRepository.save(employee1)).willReturn(employee1);

        //When
        Employee savedEmployee = employeeService.saveNewEmployee(employee1);

        //Then
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("Junit test case for save employee method which throws exception if user already exists")
    void givenExitingEmail_whenSaveNewEmployee_thenThrowsException() {

        //Given

//      testEntityManager.persistAndFlush(employee1);

        BDDMockito.given(employeeRepository.findByEmail(employee1.getEmail())).willReturn(Optional.of(employee1));

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveNewEmployee(employee1);
        });

        //Then
        verify(employeeRepository, never()).save(any(Employee.class));
        assertEquals("Employee already exists with given email: " + employee1.getEmail(), thrown.getMessage());
    }

    //Junit Test for getAllEmployees method
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList(){

        // given - preconditions or setup
        Employee employee2 = Employee.builder()
                .employeeId(2L)
                .firstName("Tom")
                .lastName("Cruise")
                .email("cruise@gmail.com")
                .build();

        Employee employee3 = Employee.builder()
                .employeeId(3L)
                .firstName("Sean")
                .lastName("Kingston")
                .email("kingston@gmail.com")
                .build();

        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee1, employee2, employee3));

        // when - action or the behaviour that we are going to test

        List<Employee> expectedList = employeeService.getAllEmloyees();

        // then - verify the output
        Assertions.assertThat(expectedList).isNotNull();
        Assertions.assertThat(expectedList.size()).isEqualTo(3);

    }

    //Junit Test for getEmployeeById Operation
    @Test
    public void givenValidEmployeeId_whenGetEmployeeById_thenReturnEmployee(){

        // given - preconditions or setup

        BDDMockito.given(employeeRepository.findById(employee1.getEmployeeId())).willReturn(Optional.of(employee1));

        // when - action or the behaviour that we are going to test

        Optional<Employee> expectedEmployee = employeeRepository.findById(employee1.getEmployeeId());

        // then - verify the output

        Assertions.assertThat(expectedEmployee).isNotNull();

    }

    //Junit Test for getEmployeeById Operation
    @Test
    public void givenInValidEmployeeId_whenGetEmployeeById_thenThrowsException(){

        // given - preconditions or setup

        BDDMockito.given(employeeRepository.findById(employee1.getEmployeeId())).willReturn(Optional.empty());

        // when - action or the behaviour that we are going to test

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByID(employee1.getEmployeeId());
        });

        // then - verify the output

        assertEquals("Not a valid Employee", thrown.getMessage());

    }
}