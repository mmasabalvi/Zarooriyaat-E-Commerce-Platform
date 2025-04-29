package com.example.zarooriyaat.Orderobj;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId; // Unique ID for the order

    @Column(nullable = false)
    private Long productId; // ID of the product in the order

    @Column(nullable = false)
    private int quantity; // Quantity of the product ordered

    @Column(nullable = false)
    private double totalPrice; // Total price of the order

    @Column(nullable = false)
    private LocalDateTime orderDate; // Date when the order was placed

    @Column(nullable = false)
    private String status; // Status of the order (e.g., Placed, Shipped, Delivered)

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

