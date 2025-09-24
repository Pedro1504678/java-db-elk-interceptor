//package com.example.teste;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//
//import com.example.teste.app.dto.GetUserDto;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class TesteApplicationTests {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    void getUsersEndpointReturnsSuccessfulResponse() {
//        // Call the GET /users endpoint
//        ResponseEntity<List> response = restTemplate.getForEntity("/users", List.class);
//
//        // Verify that the response is successful
//        assert response.getStatusCode() == HttpStatus.OK;
//
//        // Verify that the response body is not null
//        assert response.getBody() != null;
//
//        System.out.println("[DEBUG_LOG] Response status: " + response.getStatusCode());
//        System.out.println("[DEBUG_LOG] Response body: " + response.getBody());
//    }
//}
