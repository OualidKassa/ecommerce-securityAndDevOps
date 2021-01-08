package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemRepository itemRepository = mock(ItemRepository.class);
    private ItemController itemController;

    @Before
    public void configItemController() {

        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("blablabla");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("blabla description");
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName("testUser")).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void should_return_all_items(){
        final ResponseEntity<List<Item>> entities = itemController.getItems();
        Assert.assertNotNull(entities);
        Assert.assertEquals(1, entities.getBody().size());
    }

    @Test
    public void should_return_item_by_id(){
        final ResponseEntity<Item> itemResponseEntity = itemController.getItemById(1L);
        Assert.assertNotNull(itemResponseEntity);
        Assert.assertEquals(HttpStatus.OK, itemResponseEntity.getStatusCode());
    }

    @Test
    public void should_return_item_by_name(){
        final ResponseEntity<List<Item>> itemsByName = itemController.getItemsByName("testUser");
        Assert.assertNotNull(itemsByName);
        Assert.assertEquals(1, itemsByName.getBody().size());
        Assert.assertEquals(HttpStatus.OK, itemsByName.getStatusCode());
    }

    @Test
    public void should_return_error_not_found_item_by_name_with_name_not_exist(){
        final ResponseEntity<List<Item>> itemsByName = itemController.getItemsByName("falseTest");
        Assert.assertNotNull(itemsByName);
        Assert.assertEquals(HttpStatus.NOT_FOUND, itemsByName.getStatusCode());
    }


}
