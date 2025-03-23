package com.example.zarooriyaat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.zarooriyaat.productListobj.productListObject;
import com.example.zarooriyaat.repository.OrderRepository;
import com.example.zarooriyaat.Orderobj.Order;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void saveOrder(List<productListObject> cartItems) {
        double totalPrice = cartItems.stream()
                                     .mapToDouble(productListObject::getSellingPrice)
                                     .sum();

        for (productListObject product : cartItems) {
            Order order = new Order();
            order.setProductId(product.getId());
            order.setQuantity(1); // Use correct logic for quantity
            order.setTotalPrice(totalPrice);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("placed");
            orderRepository.save(order);
          
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
