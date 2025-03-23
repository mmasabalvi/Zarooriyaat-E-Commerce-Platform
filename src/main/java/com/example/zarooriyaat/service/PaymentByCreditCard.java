package com.example.zarooriyaat.service;

public class PaymentByCreditCard implements PaymentStrategy {
    @Override
    public String processPayment(double amount) {
        // Dummy functionality
        return "Payment of " + amount + " processed via Credit Card.";
    }
}
