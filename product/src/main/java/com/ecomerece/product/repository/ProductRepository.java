package com.ecomerece.product.repository;

import com.ecomerece.product.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product,Long> {
    public List<Product> findByActiveTrue();
    @Query("SELECT p FROM product p WHERE p.active = TRUE AND p.stockQuantity > 0  AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProducts(@Param("keyword") String keyword);
    Optional<Product> findByIdAndActiveTrue(Long id);
}
