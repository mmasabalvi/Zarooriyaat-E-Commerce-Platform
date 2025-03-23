package com.example.zarooriyaat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.zarooriyaat.Orderobj.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Remove the following line as userId no longer exists:
    // List<Order> findByUserId(int userId);

    // Retain only the necessary methods
    @SuppressWarnings("null")
    List<Order> findAll();
}
