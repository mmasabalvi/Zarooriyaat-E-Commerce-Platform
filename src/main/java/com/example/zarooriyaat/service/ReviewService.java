package com.example.zarooriyaat.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.zarooriyaat.productListobj.productListObject;
import com.example.zarooriyaat.reviewobj.Review;
import com.example.zarooriyaat.repository.ReviewRepository;

import com.example.zarooriyaat.repository.ProductRepository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository; // Assuming you have a ReviewRepository

    @Autowired
    private ProductRepository productRepository; // Assuming you have a ProductRepository

    /**
     * Submit a product review.
     * 
     * @param productId The ID of the product to be reviewed.
     * @param rating    The rating value (1-5).
     * @return True if the review was submitted successfully, false otherwise.
     */
    public boolean submitReview(Long productId, int rating) {
        // Validate the product ID 
        Optional<productListObject> product = productRepository.findById(productId);

        System.out.println("Submitting review for productId: " + productId + " with rating: " + rating);

        if (product.isEmpty()) {
            return false; // Invalid product ID
        }

        // Create a new review
        Review review = new Review();
        review.setProductId(productId);
        review.setRating(rating);
        review.setReviewDate(LocalDate.now());

        // Save the review to the database
        reviewRepository.save(review);
        return true;
    }

    /**
     * Retrieve reviews for a specific product.
     * 
     * @param productId The ID of the product.
     * @return A list of reviews for the product.
     */
    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    /**
     * Retrieve all reviews from the database.
     * 
     * @return A list of all reviews.
     */
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    /**
     * Delete a review by its ID.
     * 
     * @param reviewId The ID of the review to be deleted.
     * @return True if the review was deleted successfully, false otherwise.
     */
    public boolean deleteReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isPresent()) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false; // Review ID not found
    }

    /**
     * Validate a rating value.
     * 
     * @param rating The rating to be validated.
     * @return True if the rating is valid (1-5), false otherwise.
     */
    public boolean validateRating(int rating) {
        return rating >= 1 && rating <= 5;
    }
}
