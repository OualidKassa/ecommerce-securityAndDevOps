package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    CartController cartController;
    CartRepository cartRepository = mock(CartRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void configureCartTest(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
        Cart cart = new Cart();
        User user = new User();
        user.setId(0);
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setCart(cart);
        Item item = new Item();
        item.setId(1L);
        item.setName("girage");
        BigDecimal price = BigDecimal.valueOf(1456.934);
        item.setPrice(price);
        item.setDescription("bla bla bla");
        when(userRepository.findUserByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
    }

    @Test
    public void should_add_cart(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");
        final ResponseEntity<Cart> addCart = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(addCart);
        Assert.assertEquals(HttpStatus.OK, addCart.getStatusCode());
    }

    @Test
    public void should_remove_item_cart(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");
        final ResponseEntity<Cart> removeItem = cartController.removeFromcart(modifyCartRequest);
        Assert.assertNotNull(removeItem);
        Assert.assertEquals(0, removeItem.getBody().getItems().size());
    }

    @Test
    public void should_not_add_cart_if_user_not_found(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("userNotExist");
        final ResponseEntity<Cart> addCart = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(addCart);
        Assert.assertEquals(HttpStatus.NOT_FOUND, addCart.getStatusCode());
    }

    @Test
    public void should_not_add_cart_if_item_not_found(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1097L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");
        final ResponseEntity<Cart> addCart = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(addCart);
        Assert.assertEquals(HttpStatus.NOT_FOUND, addCart.getStatusCode());
    }

    @Test
    public void should_not_remove_item_cart_if_user_not_found(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("userNotExist");
        final ResponseEntity<Cart> removeItem = cartController.removeFromcart(modifyCartRequest);
        Assert.assertEquals(HttpStatus.NOT_FOUND, removeItem.getStatusCode());
    }

    @Test
    public void should_not_remove_item_cart_if_item_not_found(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(18978L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");
        final ResponseEntity<Cart> removeItem = cartController.removeFromcart(modifyCartRequest);
        Assert.assertEquals(HttpStatus.NOT_FOUND, removeItem.getStatusCode());
    }
}
