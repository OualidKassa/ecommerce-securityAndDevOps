package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    UserController userController;
    CartRepository cartRepository = mock(CartRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }


    private CreateUserRequest userRequest(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("walid");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");
        return createUserRequest;
    }

    @Test
    public void should_create_user(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("isHacked");
        final ResponseEntity<User> user = userController.createUser(userRequest());
        Assert.assertNotNull(user);
        Assert.assertEquals(HttpStatus.OK, user.getStatusCode());
    }

    @Test
    public void should_find_user_by_id(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("isHacked");
        final ResponseEntity<User> user = userController.createUser(userRequest());
        Assert.assertEquals(0, user.getBody().getId());
    }
}
