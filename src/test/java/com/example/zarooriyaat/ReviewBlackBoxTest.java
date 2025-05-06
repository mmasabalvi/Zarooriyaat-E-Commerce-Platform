package com.example.zarooriyaat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.zarooriyaat.productListobj.productListObject;
import com.example.zarooriyaat.repository.ProductRepository;
import com.example.zarooriyaat.repository.ReviewRepository;
import com.example.zarooriyaat.service.ReviewService;

@SpringBootTest
@Transactional
class ReviewBlackBoxTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Long validProductId;
    private Long invalidProductId = 999999L;

    @BeforeEach
    void setUp() {
        // Create a test product
        productListObject product = new productListObject();
        product.setName("Test Product");
        product.setSellingPrice(100.0);
        product = productRepository.save(product);
        validProductId = product.getId();
    }

    @Test
    void testSubmitReview_ValidInputs() {
        // Test Case 1: Valid product ID and rating
        assertTrue(reviewService.submitReview(validProductId, 5));
        assertEquals(1, reviewRepository.findByProductId(validProductId).size());
    }

    @Test
    void testSubmitReview_InvalidProductId() {
        // Test Case 2: Invalid product ID
        assertFalse(reviewService.submitReview(invalidProductId, 5));
        assertEquals(0, reviewRepository.findByProductId(invalidProductId).size());
    }

    @Test
    void testSubmitReview_InvalidRatingTooLow() {
        // Test Case 3: Rating below valid range
        assertFalse(reviewService.submitReview(validProductId, 0));
        assertEquals(0, reviewRepository.findByProductId(validProductId).size());
    }

    @Test
    void testSubmitReview_InvalidRatingTooHigh() {
        // Test Case 4: Rating above valid range
        assertFalse(reviewService.submitReview(validProductId, 6));
        assertEquals(0, reviewRepository.findByProductId(validProductId).size());
    }

    @Test
    void testSubmitReview_MultipleReviewsSameProduct() {
        // Test Case 5: Multiple reviews for same product
        assertTrue(reviewService.submitReview(validProductId, 4));
        assertTrue(reviewService.submitReview(validProductId, 5));
        assertEquals(2, reviewRepository.findByProductId(validProductId).size());
    }

    @Test
    void testSubmitReview_BoundaryValues() {
        // Test Case 6: Boundary value testing
        assertTrue(reviewService.submitReview(validProductId, 1));  // Minimum valid rating
        assertTrue(reviewService.submitReview(validProductId, 5));  // Maximum valid rating
        assertEquals(2, reviewRepository.findByProductId(validProductId).size());
    }

    @Test
    void testSubmitReview_NullProductId() {
        // Test Case 7: Null product ID
        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.submitReview(null, 5);
        });
    }

    @Test
    void testSubmitReview_ReviewDateValidation() {
        // Test Case 8: Verify review date is set correctly
        assertTrue(reviewService.submitReview(validProductId, 4));
        var reviews = reviewRepository.findByProductId(validProductId);
        assertEquals(1, reviews.size());
        assertNotNull(reviews.get(0).getReviewDate());
    }
} 