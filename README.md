# 🛍️ Zarooriyaat — E-Commerce Platform

**Zarooriyaat** is a full-stack e-commerce web application built using Java, Spring Boot, Thymeleaf, and MySQL. It allows customers to browse products, add them to cart, place orders, and leave reviews. Admins can manage inventory and users through a backend dashboard.


## ⚙️ Tech Stack

- **Backend:** Java 17+, Spring Boot 3.x  
- **Frontend:** HTML5, CSS3, Thymeleaf  
- **Database:** MySQL  
- **Testing:** JUnit 5, Mockito, JaCoCo  
- **Build Tool:** Maven Wrapper (`mvnw`)


## 🚀 Features

- User Registration & Login (with password hashing)
- Product Listing and Search
- Add to Cart & Checkout
- Order Placement & History
- Product Reviews by Customers
- Admin Dashboard for Product/User Management


## 🧱 System Architecture

- **Frontend:** Thymeleaf templates, HTML, CSS, JS
- **Backend:** Spring Boot (Controllers, Services, Repositories)
- **Database:** MySQL using Spring Data JPA
- **Architecture Style:** Layered (MVC pattern)

Subsystems:
- User Management
- Product Catalog
- Order Management
- Review System
- Admin Panel



## 🧪 Testing

### Controller Tests
- `Controller` and `ReviewController` tested using **MockMvc** (`@WebMvcTest`)
- Verifies routes like `/login`, `/sign-up`, `/review-product`

### ✅ DTO & Entity Tests
- Getter/setter tests for `SignupEntity`, `loginObject`, etc., to ensure model coverage

### ✅ Black-Box Testing
- Form input validation, UI rendering, and functional use cases

### ✅ Code Coverage
- Target: **80%+** using **Junit**
- Run via Maven:
  ```bash
  ./mvnw clean verify
### ✅ Code Coverage
- Target: **80%+** using **JaCoCo**
- Run via Maven:
  ```bash
  ./mvnw clean verify
  ```
- HTML Report Path:  
  ```
  target/site/jacoco/index.html
  ```

## 📁 Project Setup

```bash
# Clone repository
git clone https://github.com/yourusername/zarooriyaat.git

# Navigate to folder
cd zarooriyaat

# Run the app
./mvnw spring-boot:run

# Access in browser
http://localhost:8080
```


## 👥 Team

- Mahad Rehman Durrani  
- Masab Hammad  
- Hashim Awan