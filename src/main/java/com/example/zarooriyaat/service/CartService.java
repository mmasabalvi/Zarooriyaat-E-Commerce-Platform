package com.example.zarooriyaat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.zarooriyaat.productListobj.productListObject;
@Service
public class CartService {
    public double calculateTotalPrice(List<productListObject> products) {
        return products.stream()
                .mapToDouble(productListObject::getSellingPrice)
                .sum();
    }
}

