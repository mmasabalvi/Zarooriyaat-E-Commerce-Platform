package com.example.zarooriyaat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.zarooriyaat.productListobj.productListObject;


public interface ProductRepository extends JpaRepository<productListObject, Long> {
    List<productListObject> findAllByNameIgnoreCase(String name);
    // List<productListObject> findByCategoryIgnoreCase(String category); // Custom query to filter by category

    // @SuppressWarnings("null")
    // Optional<productListObject> findById(Long id);
   
    // @Query("SELECT p FROM productListObject p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    // List<productListObject> searchByName(@Param("name") String name);
    
    // Query to find products by category
    List<productListObject> findByCategoryIgnoreCase(String category);

    // Query to search products by name
    @Query("SELECT p FROM productListObject p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<productListObject> searchByName(@Param("name") String name);

    // Fetch product by ID
    @Override
    Optional<productListObject> findById(Long id);
}
