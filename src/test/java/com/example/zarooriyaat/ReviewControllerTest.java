package com.example.zarooriyaat;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.example.zarooriyaat.Orderobj.Order;
import com.example.zarooriyaat.service.OrderService;
import com.example.zarooriyaat.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(mainController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ReviewService reviewService;

    @Test
    void reviewPage_ShouldShowOrders() throws Exception {
        Order o = new Order();
        o.setOrderId(1);
        when(orderService.getAllOrders()).thenReturn(List.of(o));

        mockMvc.perform(get("/dashboard-customer/manage-products/review-product"))
               .andExpect(status().isOk())
               .andExpect(view().name("reviewProduct"))
               .andExpect(model().attribute("orders", List.of(o)));
    }

    @Test
    void submitReview_ValidRating_ShouldRedirect() throws Exception {
        when(reviewService.validateRating(5)).thenReturn(true);
        when(reviewService.submitReview(1L, 5)).thenReturn(true);
        when(orderService.getAllOrders()).thenReturn(List.of());

        mockMvc.perform(post("/dashboard-customer/manage-products/review-product")
                .param("productId", "1")
                .param("rating", "5"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/dashboard-customer/manage-products"));
    }

    @Test
    void submitReview_InvalidRating_ShouldStayOnPage() throws Exception {
        when(reviewService.validateRating(6)).thenReturn(false);

        mockMvc.perform(post("/dashboard-customer/manage-products/review-product")
                .param("productId", "1")
                .param("rating", "6"))
               .andExpect(status().isOk())
               .andExpect(view().name("reviewProduct"))
               .andExpect(model().attributeExists("error"));
    }
}
