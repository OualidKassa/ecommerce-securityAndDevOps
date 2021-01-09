package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("anotherUser")).thenReturn(null);
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
    public void should_find_user_by_username(){
        final ResponseEntity<User> findUser = userController.findByUserName("testUser");
        Assert.assertEquals(HttpStatus.OK, findUser.getStatusCode());
        Assert.assertEquals("testPassword", findUser.getBody().getPassword());
    }

    @Test
    public void should_find_user_by_id(){
        final ResponseEntity<User> findUser = userController.findById(0l);
        Assert.assertEquals(HttpStatus.OK, findUser.getStatusCode());
    }

    @Test
    public void should_not_find_user_by_wrong_id(){
        final ResponseEntity<User> findNoUser = userController.findById(9L);
        Assert.assertEquals(HttpStatus.NOT_FOUND, findNoUser.getStatusCode());
    }

    @Test
    public void should_not_find_user_with_wrong_username(){
        final ResponseEntity<User> findNoUser = userController.findByUserName("wrongName");
        Assert.assertEquals(HttpStatus.NOT_FOUND, findNoUser.getStatusCode());
    }

    @Test
    public void should_not_create_user_with_password_less_than_seven_char(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("walid");
        createUserRequest.setPassword("test");
        createUserRequest.setConfirmPassword("test");
        final ResponseEntity<User> user = userController.createUser(createUserRequest);
        Assert.assertNotNull(user);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, user.getStatusCode());
    }
}
