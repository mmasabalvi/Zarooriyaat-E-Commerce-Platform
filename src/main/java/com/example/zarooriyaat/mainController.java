package com.example.zarooriyaat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.zarooriyaat.categoryobj.categoryObject;
import com.example.zarooriyaat.productListobj.productListObject;
import com.example.zarooriyaat.repository.SignupRepository;
import com.example.zarooriyaat.service.ProductService;
import com.example.zarooriyaat.signupobj.SignupEntity;

@Controller
public class mainController {
    
    // ------------Decalarations--------------//
    private String loggedInUserEmail;
    @Autowired
    private SignupRepository signupRepository;
    @Autowired
    private ProductService productService;
    
    // main page
    @GetMapping("/main")
    public String showMainPage() {
        return "index"; // Refers to templates/login.html
    }
    
    // LOGIN PAGE
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginObject", new SignupEntity());
        return "login"; // Refers to templates/login.html
    }

    // LOGIN PAGE
    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("loginObject") SignupEntity loginDto,
            RedirectAttributes redirectAttributes) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        loggedInUserEmail = email; // Store the email in the global variable

        // Authenticate the user
        Optional<SignupEntity> user = signupRepository.findByEmailAndPassword(email, password);

        if (user.isPresent()) {
            // Login successful
            redirectAttributes.addFlashAttribute("username", user.get().getFullName());
            return "redirect:/categories"; // Redirect to /categories
        } else {
            // Login failed
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/login"; // Redirect to login with error
        }
    }

    // SIGNUP PAGE
    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("signupObject", new SignupEntity());
        return "signup"; // Refers to templates/signup.html
    }

    @PostMapping("/signup")
    public String handleSignUp(@ModelAttribute("signupObject") SignupEntity signupObj, Model model) {
        try {
            // Validate that the password and confirmPassword match
            if (!signupObj.getPassword().equals(signupObj.getConfirmPassword())) {
                model.addAttribute("error", "Passwords do not match.");
                return "signup"; // Return to signup page with an error message
            }

            // Map signupObject to SignupEntity
            SignupEntity entity = new SignupEntity();
            entity.setFullName(signupObj.getFullName());
            entity.setEmail(signupObj.getEmail());
            entity.setPhoneNumber(signupObj.getPhoneNumber());
            entity.setPassword(signupObj.getPassword()); // Add password hashing in production

            // Save the entity to the database
            signupRepository.save(entity);

            // Redirect to login page
            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            model.addAttribute("error", "An error occurred during signup.");
            return "signup"; // Return to signup page with error message
        }
    }

    // CATEGORIES PAGE
    @GetMapping("/categories")
    public String showCategoriesPage(Model model) {
        List<categoryObject> categories = new ArrayList<>();
        categories.add(new categoryObject("Electronics", "../img/laptop.jpeg", "/products/electronics"));
        categories.add(new categoryObject("Clothing", "../img/apparel.jpeg", "/products/clothing"));
        categories.add(new categoryObject("Home and Kitchen", "../img/food.jpeg", "/products/home"));
        categories.add(new categoryObject("Health and Fitness", "../img/gym.jpeg", "/products/health"));

        model.addAttribute("categories", categories);
        return "category"; // Refers to templates/category.html
    }

    // Product List Page
    @GetMapping("/productList")
    public String showProductList(
            @RequestParam(name = "category", required = false) String category,
            Model model) {
        // Retrieve products from the database
        List<productListObject> products = productService.getProducts(category);

        // // Ensure each product has a valid 'id'
        // for (productListObject product : products) {
        // System.out.println("Product ID: " + product.getId()); // Debugging
        // }

        model.addAttribute("products", products);
        model.addAttribute("category", category != null ? category : "All Products");

        return "productList";
    }


}
