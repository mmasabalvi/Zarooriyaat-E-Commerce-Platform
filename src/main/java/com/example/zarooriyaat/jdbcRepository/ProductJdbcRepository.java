package com.example.zarooriyaat.jdbcRepository;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import com.example.zarooriyaat.productListobj.productListObject;

@Repository
public class ProductJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("deprecation")
    public List<productListObject> findProducts(String category) {
        String sql = "SELECT id, name, brand, image_url, stock_status, selling_price, original_price, category, description FROM products";
        List<Object> params = new ArrayList<>();

        // Add a WHERE clause if a category is provided
        if (category != null && !category.isEmpty()) {
            sql += " WHERE category = ?";
            params.add(category);
        }

        // Use the updated query method
        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> {
            productListObject product = new productListObject(
                    rs.getString("name"),
                    rs.getString("brand"),
                    rs.getString("image_url"),
                    rs.getString("stock_status"),
                    rs.getDouble("selling_price"),
                    rs.getDouble("original_price"),
                    rs.getString("category"),
                    rs.getString("description")
            );
            // Set the ID
            product.setId(rs.getLong("id"));
            return product;
        });
    }
}
