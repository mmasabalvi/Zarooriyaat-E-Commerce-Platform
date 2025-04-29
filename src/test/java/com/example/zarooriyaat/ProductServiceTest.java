package com.example.zarooriyaat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.zarooriyaat.jdbcRepository.ProductJdbcRepository;
import com.example.zarooriyaat.productListobj.productListObject;
import com.example.zarooriyaat.repository.ProductRepository;
import com.example.zarooriyaat.service.ProductService;

// .\mvnw.cmd test

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductJdbcRepository productJdbcRepository;

    @InjectMocks
    private ProductService productService;

    private productListObject sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new productListObject();
        sampleDto.setId(1L);
        sampleDto.setName("Sample");
        sampleDto.setSellingPrice(100.00);
    }

    @Test
    void testGetInventoryForSeller() {
        // Arrange
        List<productListObject> expected = Arrays.asList(sampleDto);
        when(productRepository.findAll()).thenReturn(expected);

        // Act
        List<productListObject> actual = productService.getInventoryForSeller();

        // Assert
        assertEquals(expected, actual, "Should return all products from repository");
        verify(productRepository).findAll();
    }

    @Test
    void testGetProductsByCategory() {
        // Arrange
        String category = "Electronics";
        List<productListObject> expected = Arrays.asList(sampleDto);
        when(productJdbcRepository.findProducts(category)).thenReturn(expected);

        // Act
        List<productListObject> actual = productService.getProducts(category);

        // Assert
        assertEquals(expected, actual, "Should return products from JDBC repository");
        verify(productJdbcRepository).findProducts(category);
    }

    @Test
    void testGetProductById() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleDto));

        // Act
        productListObject actual = productService.getProductById(1L);

        // Assert
        assertNotNull(actual, "Product should not be null");
        assertEquals(sampleDto.getName(), actual.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void testSaveProduct() {
        // Arrange
        // productRepository.save returns the saved entity
        when(productRepository.save(sampleDto)).thenReturn(sampleDto);

        // Act
        productService.saveProduct(sampleDto);

        // Assert
        verify(productRepository).save(sampleDto);
    }

    @Test
    void testSearchProducts() {
        // Arrange
        String query = "Test";
        List<productListObject> expected = Arrays.asList(sampleDto);
        when(productRepository.searchByName(query)).thenReturn(expected);

        // Act
        List<productListObject> actual = productService.searchProducts(query);

        // Assert
        assertEquals(expected, actual, "Should return matching products");
        verify(productRepository).searchByName(query);
    }
}
