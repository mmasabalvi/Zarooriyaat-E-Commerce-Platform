package com.example.zarooriyaat.service;


public class PaymentByPaypal implements PaymentStrategy {
    @Override
    public String processPayment(double amount) {
        // Dummy functionality
        return "Payment of " + amount + " processed via PayPal.";
    }
}

