package com.TestingTutorials.springboottesting.BaseIntegration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public abstract class BaseIntegrationTestContainer {

    /**
     * This is an implementation of singleton's
     * containers pattern for our testContainers.
     * This is particularly important due to the
     * reuse of the docker containers for all integration tests,
     * Basically to avoid redundancy.
     *
     * **/

    static final PostgreSQLContainer postgresqlContainer;

    // Database container from TestContainers would be shared between test classes by adding static
    static {
        postgresqlContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:13.1"))
                .withDatabaseName("test_employee_RestAPI")
                .withPassword("password")
                .withUsername("postgres");

        postgresqlContainer.start();
    }


    // Make the Results of the container available to the Application context using Dynamic properties
    @DynamicPropertySource
    public static void dynamicProperty(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }
}
