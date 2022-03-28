package com.TestingTutorials.springboottesting.IntegrationTests;


import com.TestingTutorials.springboottesting.Data.Entities.Employee;
import com.TestingTutorials.springboottesting.Data.Repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 *
 * To effectively write integration tests, this is what I know:
 *  - Integration test must be separate from unit test and should not be run together.
 *  - We use @SpringBootTest to perform integration test
 *  - Setting up a webEnvironment is crucial for most integration tests (why?)
 *  - You never mock anything, every dependency should be autowired in directly.
 *  - You should use a database that is as close to your prod DB in your unit tests.
 *  - Use real data ? Use captured data from a workflow?
 *  - What are the best practices?
 *  - Your database should always be fresh for each new integration test, best practice is to clear out the database
 *  before each test.
 *  - The best approach to integration step should be a comprehensive unit test of the controller layer,
 *  this is because an integration test is mostly a controller unit test that has no mocks/stubbing.
 *
 * **/

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private Employee employee;
    private Employee employee1;
    private Employee employee2;
    List<Employee> listOfEmployees = new ArrayList<>();

    @BeforeEach
    void cleanup() {
        employeeRepository.deleteAll();
    }

    @BeforeEach
    void setup() {

        // Setup frequently/commonly used instances or configs
        employee = Employee.builder()
                .firstName("Emmanuel")
                .lastName("Iwu")
                .email("iwuemmanuel64@gmail.com")
                .build();
        employee1 = Employee.builder()
                .firstName("Ihab")
                .lastName("Ezourah")
                .email("player@gmail.com")
                .build();
        employee2 = Employee.builder()
                .firstName("Yassine")
                .lastName("Mohammed")
                .email("jalabiyaPlayer@gmail.com")
                .build();

        listOfEmployees.add(employee);
        listOfEmployees.add(employee1);
        listOfEmployees.add(employee2);
    }

    @Test
    @DisplayName(" Integration test case for createEmployee REST API ")
    void givenEmployeeObject_whenCreateEmployee_thenReturnNewEmployee() throws Exception {
        // Given --> Precondition and setup

        /**
         *  This is basically the same test case for controller unit test with the important difference
         *  that it does not mock any method calls, it just takes a valid input from our setup and makes
         *  the needed requests to the correct endpoints and verifies the response.
         * **/

        // When the action request call is made
        ResultActions postRequestResult = mockMvc.perform(post("/api/employees/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //Then verify the result of the above post request

/*        postRequestResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
*/
        postRequestResult.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    @Test
    @DisplayName(" Integration test case for getAllEmployees REST API ")
    void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {

        // Given --> Precondition and setup
        employeeRepository.saveAll(listOfEmployees);

        // When the action request call is made
        ResultActions responseList = mockMvc.perform(get("/api/employees/getAllEmployees"));

        // Then Verify the output
        responseList.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",
                        CoreMatchers.is(listOfEmployees.size())));
    }


    @Test
    @DisplayName("Integration Test for Valid case of getEmployeeById REST API")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject () throws Exception{

        // given - preconditions or setup

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test

        ResultActions responseObject = mockMvc.perform(get("/api/employees/{id}", employee.getEmployeeId()));

        // then - verify the output
        responseObject.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));

    }


    @Test
    //@ExceptionHandler(ResourceNotFoundException.class)
    @DisplayName("Integration Test for Invalid case of getEmployeeById REST API")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNullOrEmpty() throws Exception {

        // given - preconditions or setup
        long invalidEmployeeId = 5L;

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test



        ResultActions responseObject = mockMvc.perform(get("/api/employees/{id}", invalidEmployeeId));

        // then - verify the output
        responseObject.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

//        .andExpect(jsonPath("$.response.errors[0].message", CoreMatchers.is("Not a valid Employee")))  For Exception handling

    }


    @Test
    @DisplayName("Integration Test case for Valid UpdateEmployee REST API")
    public void givenValidEmployeeId_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception{

        // given - preconditions or setup

        employeeRepository.save(employee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Warda")
                .lastName("Zineb")
                .email("warda@geomatic.ma")
                .build();

        // when - action or the behaviour that we are going to test

        ResultActions responseObject = mockMvc.perform(put("/api/employees/{id}", employee.getEmployeeId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        responseObject.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is("Warda")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is("warda@geomatic.ma")));

    }

    @Test
    @DisplayName("Integration Test for Invalid case of UpdateEmployeeById REST API")
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnNullOrEmptyObject() throws Exception{

        // given - preconditions or setup

        long invalidEmployeeId = 5L;

        employeeRepository.save(employee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Warda")
                .lastName("Zineb")
                .email("warda@geomatic.ma")
                .build();

        // when - action or the behaviour that we are going to test

        ResultActions responseObject = mockMvc.perform(put("/api/employees/{id}", invalidEmployeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        responseObject.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DisplayName("Integration Test case for deleteEmployee REST API")
    void givenEmployeeId_whenDeleteEmployee_thenReturnStatusConfirmationMessage() throws Exception {

        // given - preconditions or setup
        employeeRepository.save(employee);
        // when - action or the behaviour that we are going to test

        ResultActions responseObject = mockMvc.perform(delete("/api/employees/{id}", employee.getEmployeeId()));

        // then - verify the output
        responseObject.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
