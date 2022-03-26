package com.TestingTutorials.springboottesting.controllers;

import com.TestingTutorials.springboottesting.Data.Entities.Employee;
import com.TestingTutorials.springboottesting.services.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest
class EmployeeControllerTest {

    /**
     * For Controller test, a few things needs to be in your mind:
     * - @WebMvcTest annotation that makes it possible for you to test controllers and
     * provides all the needed beans for making and testing api calls.
     * - You need to figure out your dependencies ==> Services most likely culprits
     * - Dependencies needs to be mocked but using @MockBean annotation, to allow spring create
     * a mock of the dependencies and add the mocks to the application context, making it available for
     * the controller Tests.
     * - MockMvc object that provides methods for performing http calls
     * - ObjectMappers from Jackson that provides you with the means to serialize and deserialize POJOS
     *
     *
     * General workflow:
     * - Given valid inputs for the http requests ==> From the controller method arguments
     * - BDD --> Behaviour driven development workflow that checks a given function call with
     * the right inputs using ArgumentMatchers from the class/Entity under test and returns an expected
     * result or results if given multiple arguments.
     * - When a given method under test is called and fed the correct inputs returns a response that is used for
     * assertions.
     * - Then we make assertions on the response from the requests to verify that the correct responses are
     * returned including the right status codes.
     *
     **/

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private  Employee employee;
    private  Employee employee1;
    private  Employee employee2;
    List<Employee> listOfEmployees = new ArrayList<>();


    @BeforeEach
    void setUp() {

        // Setup frequently/commonly used instances or configs
        employee = Employee.builder()
                .employeeId(1L)
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
    @DisplayName(" Junit test case for createEmployee REST API ")
    void givenEmployeeObject_whenCreateEmployee_thenReturnNewEmployee() throws Exception {
        // Given --> Precondition and setup

        /** This precondition states that given the return statement of the createEmployee Api,
        *  the saveNewEmployee service function will return an answer that is an invocation of the
        *  service function with the number of arguments given to the controller function
        * **/
        BDDMockito.given(employeeService.saveNewEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    @Test
    @DisplayName(" Junit test case for getAllEmployees REST API ")
    void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {

        // Given --> Precondition and setup
        BDDMockito.given(employeeService.getAllEmloyees()).willReturn(listOfEmployees);

        // When the action request call is made
        ResultActions responseList = mockMvc.perform(get("/api/employees/getAllEmployees"));

        // Then Verify the output
        responseList.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(listOfEmployees.size())));
    }

    //Junit Test for Valid case of getEmployeeById REST API
    @Test
    @DisplayName("Junit Test for Valid case of getEmployeeById REST API")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject () throws Exception{

        // given - preconditions or setup

        BDDMockito.given(employeeService.getEmployeeByID(employee.getEmployeeId())).willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going to test

        ResultActions responseObject = mockMvc.perform(get("/api/employees/{id}", employee.getEmployeeId()));

        // then - verify the output
        responseObject.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));

    }

    //Junit Test for Invalid case of getEmployeeById REST API
    @Test
    @DisplayName("Junit Test for Invalid case of getEmployeeById REST API")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNullOrEmpty() throws Exception{

        // given - preconditions or setup

        BDDMockito.given(employeeService.getEmployeeByID(employee.getEmployeeId())).willReturn(Optional.empty());

        // when - action or the behaviour that we are going to test

        ResultActions responseObject = mockMvc.perform(get("/api/employees/{id}", employee.getEmployeeId()));

        // then - verify the output
        responseObject.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    //Junit Test for Valid case of UpdateEmployeeById REST API
    @Test
    @DisplayName("Junit Test case for Valid UpdateEmployee REST API")
    public void givenValidEmployeeId_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception{

        // given - preconditions or setup

        Employee updatedEmployee = Employee.builder()
                .firstName("Warda")
                .lastName("Zineb")
                .email("warda@geomatic.ma")
                .build();

        BDDMockito.given(employeeService.getEmployeeByID(employee.getEmployeeId())).willReturn(Optional.of(employee));

        BDDMockito.given(employeeService.UpdateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));


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

    //Junit Test for Invalid case of UpdateEmployeeById REST API
    @Test
    @DisplayName("Junit Test for Invalid case of UpdateEmployeeById REST API")
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnNullOrEmptyObject() throws Exception{

        // given - preconditions or setup

        Employee updatedEmployee = Employee.builder()
                .firstName("Warda")
                .lastName("Zineb")
                .email("warda@geomatic.ma")
                .build();

        BDDMockito.given(employeeService.getEmployeeByID(employee.getEmployeeId())).willReturn(Optional.empty());

        BDDMockito.given(employeeService.UpdateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));


        // when - action or the behaviour that we are going to test

        ResultActions responseObject = mockMvc.perform(put("/api/employees/{id}", employee.getEmployeeId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        responseObject.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    //Junit Test case for deleteEmployee REST API
    @Test
    @DisplayName("Junit Test case for deleteEmployee REST API")
    void givenEmployeeId_whenDeleteEmployee_thenReturnStatusConfirmationMessage() throws Exception {

        // given - preconditions or setup
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(employee.getEmployeeId());

        // when - action or the behaviour that we are going to test

        ResultActions responseObject = mockMvc.perform(delete("/api/employees/{id}", employee.getEmployeeId()));

        // then - verify the output
        responseObject.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}