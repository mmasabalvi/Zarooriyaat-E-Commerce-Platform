package com.example.zarooriyaat.service;

public class PaymentContext {
    private PaymentStrategy paymentStrategy;

    // Set the strategy at runtime
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public String executePayment(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy is not set.");
        }
        return paymentStrategy.processPayment(amount);
    }
}
