package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private OrderController orderController;

    @Before
    public void configOrderController(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        Cart cart = new Cart();
        User user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setId(1L);
        item.setName("blablabla");
        BigDecimal price = BigDecimal.valueOf(976.98);
        item.setPrice(price);
        item.setDescription("blabla description");
        cart.setTotal(price);
        cart.setId(1L);
        cart.setUser(user);

        user.setCart(cart);
        List<Item> items = new ArrayList<Item>();
        items.add(item);
        cart.setItems(items);

        when(userRepository.findUserByUsername("testUser")).thenReturn(user);
    }

    @Test
    public void should_submit_user_order_with_username() {
        final ResponseEntity<UserOrder> submitUserOrder = orderController.submit("testUser");
        Assert.assertEquals(HttpStatus.OK, submitUserOrder.getStatusCode());
        Assert.assertEquals(1, submitUserOrder.getBody().getUser().getId());
    }

    @Test
    public void should_return_order_for_user_with_username(){
        final ResponseEntity<List<UserOrder>> orderListUser = orderController.getOrdersForUser("testUser");
        Assert.assertEquals(HttpStatus.OK, orderListUser.getStatusCode());
    }

    @Test
    public void should_return_error_order_for_user_with_wrong_username(){
        final ResponseEntity<List<UserOrder>> orderListUser = orderController.getOrdersForUser("wrongUser");
        Assert.assertEquals(HttpStatus.NOT_FOUND, orderListUser.getStatusCode());
    }

    @Test
    public void should_not_submit_user_order_with_wrong_username() {
        final ResponseEntity<UserOrder> submitUserOrder = orderController.submit("wrongUser");
        Assert.assertEquals(HttpStatus.NOT_FOUND, submitUserOrder.getStatusCode());
    }
}
