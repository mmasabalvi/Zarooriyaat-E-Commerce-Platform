package com.example.zarooriyaat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.zarooriyaat.productAddobj.product;
import com.example.zarooriyaat.productListobj.productListObject;
import com.example.zarooriyaat.repository.ReviewRepository;
import com.example.zarooriyaat.repository.ProductRepository;
import com.example.zarooriyaat.service.ReviewService;


// .\mvnw.cmd test

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewService reviewService;

    private final Long PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        // nothing special to initialize
    }

    @Test
    void testValidateRating_Valid() {
        assertTrue(reviewService.validateRating(3));
    }

    @Test
    void testValidateRating_InvalidLow() {
        assertFalse(reviewService.validateRating(0));
    }

    @Test
    void testValidateRating_InvalidHigh() {
        assertFalse(reviewService.validateRating(6));
    }

    @Test
    void testSubmitReview_Success() {
        // Arrange: stub the DTO‐based repo
        productListObject dto = new productListObject();
        dto.setId(PRODUCT_ID);
        // … set any other mandatory fields …
        when(productRepository.findById(PRODUCT_ID))
            .thenReturn(Optional.of(dto));          // <-- use productListObject, not product

        // Act
        boolean ok = reviewService.submitReview(PRODUCT_ID, 5);

        // Assert
        assertTrue(ok);
        verify(reviewRepository).save(any());
    }


    @Test
    void testSubmitReview_Failure_NoProduct() {
        // Arrange: product not found
        when(productRepository.findById(PRODUCT_ID))
            .thenReturn(Optional.empty());

        // Act
        boolean result = reviewService.submitReview(PRODUCT_ID, 4);

        // Assert
        assertFalse(result);
        verify(reviewRepository, never()).save(any());
    }
}
