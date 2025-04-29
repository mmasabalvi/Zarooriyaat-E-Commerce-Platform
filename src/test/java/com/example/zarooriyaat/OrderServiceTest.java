package com.example.zarooriyaat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.zarooriyaat.Orderobj.Order;
import com.example.zarooriyaat.productListobj.productListObject;
import com.example.zarooriyaat.repository.OrderRepository;
import com.example.zarooriyaat.service.OrderService;


// .\mvnw.cmd test

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testGetAllOrders() {
        // Arrange
        Order o1 = new Order();
        o1.setOrderId(1);
        o1.setProductId(10L);
        o1.setQuantity(2);
        o1.setTotalPrice(200.0);
        o1.setOrderDate(LocalDateTime.now());
        o1.setStatus("placed");

        Order o2 = new Order();
        o2.setOrderId(2);
        o2.setProductId(20L);
        o2.setQuantity(1);
        o2.setTotalPrice(150.0);
        o2.setOrderDate(LocalDateTime.now());
        o2.setStatus("placed");

        when(orderRepository.findAll()).thenReturn(Arrays.asList(o1, o2));

        // Act
        List<Order> orders = orderService.getAllOrders();

        // Assert
        assertEquals(2, orders.size(), "Should return two orders");
        assertEquals(1, orders.get(0).getOrderId());
        assertEquals(2, orders.get(1).getOrderId());
        verify(orderRepository).findAll();
    }

    @Test
    void testSaveOrder() {
        // Arrange: create cart items
        productListObject p1 = new productListObject();
        p1.setId(10L);
        p1.setSellingPrice(100.0);

        productListObject p2 = new productListObject();
        p2.setId(20L);
        p2.setSellingPrice(150.0);

        List<productListObject> cart = Arrays.asList(p1, p2);

        // Act
        orderService.saveOrder(cart);

        // Assert: capture saved orders
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(2)).save(captor.capture());

        List<Order> savedOrders = captor.getAllValues();
        double expectedTotal = 100.0 + 150.0;

        // First saved order assertions
        Order saved1 = savedOrders.get(0);
        assertEquals(10L, saved1.getProductId());
        assertEquals(1, saved1.getQuantity());
        assertEquals(expectedTotal, saved1.getTotalPrice());
        assertEquals("placed", saved1.getStatus());
        assertNotNull(saved1.getOrderDate());

        // Second saved order assertions
        Order saved2 = savedOrders.get(1);
        assertEquals(20L, saved2.getProductId());
        assertEquals(1, saved2.getQuantity());
        assertEquals(expectedTotal, saved2.getTotalPrice());
        assertEquals("placed", saved2.getStatus());
        assertNotNull(saved2.getOrderDate());
    }
}
