package com.example.zarooriyaat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.zarooriyaat.productAddobj.product;

public interface ProductAddRepository extends JpaRepository<product, Long> {
    List<product> findByCategoryIgnoreCase(String category); // Custom query to filter by category
    List<product> findAllByNameIgnoreCase(String name);

    @SuppressWarnings("null")
    Optional<product> findById(Long id);
}
