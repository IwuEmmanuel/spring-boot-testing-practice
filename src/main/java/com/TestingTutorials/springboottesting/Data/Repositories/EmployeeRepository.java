package com.TestingTutorials.springboottesting.Data.Repositories;

import com.TestingTutorials.springboottesting.Data.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Custom queries using Query Method Approach in spring Data JPA
    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByFirstName(String firstName);

    // Custom Query using JPQL with index params ==> Java persistence query language
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByFirstNameAndLastName(String firstName, String lastName);

    // Custom Query using JPQL with Named params ==> Note: The param names must be the same as the query result name
    @Query("select e from Employee e where e.firstName =:firstName and e.email = :email")
    Employee findByFirstNameAndEmail(@Param("firstName") String firstName, @Param("email") String email);

    // Custom Query using NativeSQL with index params
    @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByNativeSQL(String firstName, String email);

    // Custom Query using NativeSQL with named params ==> Note: The param names must be the same as the query result name
    @Query(value = "select * from employees e where e.first_name = :first_name and e.last_name = :last_name", nativeQuery = true)
    Employee findByNativeSQLWithNamedParams (@Param("first_name") String firstName, @Param("last_name") String email);


}
