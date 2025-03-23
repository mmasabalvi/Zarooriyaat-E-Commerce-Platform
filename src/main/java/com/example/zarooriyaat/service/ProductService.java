package com.example.zarooriyaat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.zarooriyaat.jdbcRepository.ProductJdbcRepository;
import com.example.zarooriyaat.productListobj.productListObject;
import com.example.zarooriyaat.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductJdbcRepository productJdbcRepository;

    public ProductService(ProductRepository productRepository,ProductJdbcRepository productJdbcRepository) {
        this.productRepository = productRepository;
        this.productJdbcRepository = productJdbcRepository;
    }

    // This method should fetch the products for a specific seller
    public List<productListObject> getInventoryForSeller() {
        // Fetch products from the database for a specific seller
        // If you have a seller identifier (like sellerId), you can filter by it
        return productRepository.findAll(); // Or apply necessary filters to get the seller's inventory
    }

    public List<productListObject> getProducts(String category) {
        // Delegate product retrieval to JDBC repository
        return productJdbcRepository.findProducts(category);
    }


    public productListObject getProductById(Long id)
    {
        //return productRepository.findById(id);
        Optional<productListObject> result = productRepository.findById(id);
        productListObject product = result.get();
        return product;
        
    }

    // Insert products into the database
    public void saveProduct(productListObject product) {
        productRepository.save(product);
    }
    // public List<productListObject> searchProducts(String query) {
    //     return productRepository.findAllByNameIgnoreCase(query);
    // }
    public List<productListObject> searchProducts(String query) {
        // Call the repository method for partial matching
        return productRepository.searchByName(query);
    }
}
