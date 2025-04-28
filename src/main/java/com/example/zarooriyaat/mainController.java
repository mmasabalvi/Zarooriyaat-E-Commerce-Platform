package com.example.zarooriyaat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.zarooriyaat.categoryobj.categoryObject;
import com.example.zarooriyaat.productAddobj.product;
import com.example.zarooriyaat.productListobj.productListObject;
import com.example.zarooriyaat.repository.ProductAddRepository;
import com.example.zarooriyaat.repository.SellerSignupRepository;
import com.example.zarooriyaat.repository.SignupRepository;
import com.example.zarooriyaat.service.CartService;
import com.example.zarooriyaat.service.OrderService;
import com.example.zarooriyaat.service.PaymentByCreditCard;
import com.example.zarooriyaat.service.PaymentByPaypal;
import com.example.zarooriyaat.service.PaymentContext;
import com.example.zarooriyaat.service.ProductService;
import com.example.zarooriyaat.signupobj.SignupEntity;
import com.example.zarooriyaat.signupsellerobj.SellerSignupEntity;


@Controller
public class mainController {
    
    // ------------Decalarations--------------//
    private String loggedInUserEmail;
    @Autowired
    private SignupRepository signupRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private SellerSignupRepository sellerSignupRepository;
        
    @Autowired
    private CartService cartService;
    private List<Long> cart = new ArrayList<>();

    @Autowired
    private ProductAddRepository productAddRepository;


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

        // Product View Mapping
    @GetMapping("/productView/{id}")
    public String showProductPage(@PathVariable Long id, Model model) {
        // Retrieve product details from the database by name
        // productListObject product = productService.getProductByName(id);
        productListObject product = productService.getProductById(id);

        if (product == null) {
            // If the product is not found, redirect to an error page or handle gracefully
            model.addAttribute("error", "Product not found");
            return "error"; // Refers to a generic error page
        }

        // Pass product details to the model
        model.addAttribute("product", product);

        return "productView"; // Refers to templates/productView.html
    }

    // Add to Cart Endpoint
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("productId") Long productId, RedirectAttributes redirectAttributes) {
        cart.add(productId); // Add product ID to the cart
        redirectAttributes.addFlashAttribute("message", "Item added to cart successfully!");

        return "redirect:/productList"; // Redirect to product list page
    }    


    @GetMapping("/cart")
    public String viewCart(Model model) {
        List<productListObject> cartItems = cart.stream()
                .map(productService::getProductById)
                .collect(Collectors.toList());

        double totalPrice = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    // CHECKOUT PAGE
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model) {
        List<productListObject> cartItems = cart.stream()
                .map(productService::getProductById)
                .collect(Collectors.toList());

        double totalPrice = cartService.calculateTotalPrice(cartItems);

        List<String> paymentMethods = List.of("Credit Card", "PayPal");

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentMethods", paymentMethods);

        return "checkout"; // Refers to templates/checkout.html
    }

    @PostMapping("/process-payment")
    public String processPayment(
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            Model model) {

        PaymentContext paymentContext = new PaymentContext();
        double totalPrice = cartService.calculateTotalPrice(cart.stream()
                .map(productService::getProductById)
                .collect(Collectors.toList()));

        switch (paymentMethod) {
            case "Credit Card":
                paymentContext.setPaymentStrategy(new PaymentByCreditCard());
                break;
            case "PayPal":
                paymentContext.setPaymentStrategy(new PaymentByPaypal());
                break;
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }

        String paymentResult = paymentContext.executePayment(totalPrice);

        // Save cart items as order
        orderService.saveOrder(cart.stream().map(productService::getProductById).collect(Collectors.toList()));

        // Clear cart
        cart.clear();

        model.addAttribute("paymentResult", paymentResult);
        model.addAttribute("message", "Order placed successfully!");

        return "payment-success"; // Redirect to the confirmation page

    }



    //Adding Seller Logics

    // Display Seller Signup Page
    @GetMapping("/signup-seller")
    public String showSellerSignUpPage(Model model) {
        model.addAttribute("signupObject", new SellerSignupEntity());
        return "signupSeller"; // Refers to templates/signupSeller.html
    }

    // Handle Seller Signup Form Submission
    @PostMapping("/signup-seller")
    public String handleSellerSignUp(@ModelAttribute("signupObject") SellerSignupEntity signupObj, Model model) {
        try {
            // Save the entity to the database
            sellerSignupRepository.save(signupObj);

            // Redirect to login page after successful signup
            return "redirect:/login-seller";
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            model.addAttribute("errorMessage", "An error occurred during signup.");
            return "signupSeller"; // Return to signupSeller page with error message
        }
    }

    @GetMapping("/login-seller")
    public String showSellerLoginPage(Model model) {
        model.addAttribute("loginObject", new SellerSignupEntity()); // Make sure this matches your model name in the
                                                                     // Thymeleaf
        return "loginSeller"; // Correctly point to the seller login template
    }

    @PostMapping("/login-seller")
    public String handleSellerLogin(@ModelAttribute("loginObject") SellerSignupEntity loginDto,
            RedirectAttributes redirectAttributes) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        // Authenticate the user
        Optional<SellerSignupEntity> user = sellerSignupRepository.findByEmailAndPassword(email, password);

        if (user.isPresent()) {
            // Login successful
            redirectAttributes.addFlashAttribute("username", user.get().getFullName());
            return "redirect:/dashboard-seller"; // Redirect to /categories
        } else {
            // Login failed
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/signup-seller"; // Redirect to login with error
        }
    }    

    @GetMapping("/dashboard-seller")
    public String showSellerDashboard(Model model) {
        // Fetch inventory data for the seller (this will be a list of products)
        List<productListObject> inventory = productService.getInventoryForSeller();
        
        // Add the inventory data to the model
        model.addAttribute("inventory", inventory);
        
        // Return the view for the seller dashboard
        return "sellerDashboard"; // This points to your Thymeleaf template
    }

    @GetMapping("/dashboard-seller/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new product());

        // check if produt already esixts

        // else
        return "addProduct";
    }

    @PostMapping("/dashboard-seller/add-product")
    public String handleAddProduct(@ModelAttribute product product, RedirectAttributes redirectAttributes) {
        productAddRepository.save(product);
        redirectAttributes.addFlashAttribute("message", "Product added successfully!");
        return "redirect:/dashboard-seller";
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        // Log the query for debugging
        System.out.println("Search query: " + query);

        // Search products using the service layer
        List<productListObject> searchResults = productService.searchProducts(query);

        // Debugging: Log the results
        if (searchResults.isEmpty()) {
            System.out.println("No products found for query: " + query);
        } else {
            searchResults.forEach(product -> System.out.println("Found product: " + product.getName()));
        }

        // Add search results to the model
        model.addAttribute("products", searchResults);

        // Return the view (productList.html)
        return "productList";
    }    

    @GetMapping("/dashboard-customer")
    public String showCustomerDashboard(Model model) {
        model.addAttribute("userName", "Customer Name"); // Replace with actual logic
        return "customerDashboard"; // Map to the Thymeleaf template name
    }
    
}
