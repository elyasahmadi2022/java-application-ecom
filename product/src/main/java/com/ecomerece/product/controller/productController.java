package com.ecomerece.product.controller;

import com.ecomerece.product.dto.ProductRequest;
import com.ecomerece.product.dto.ProductResponse;
import com.ecomerece.product.dto.ProductResponse;
import com.ecomerece.product.services.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class productController {
    private final ProductServices productServices;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
            return new ResponseEntity<>(productServices.getProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        System.out.println(id);
        return productServices.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest product) {
        return new ResponseEntity<>(productServices.createProduct(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,@RequestBody ProductRequest product) {
        return productServices.updateProduct(id, product).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productServices.deleteProduct(id);
        return isDeleted ?  ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        System.out.print(keyword);
        return new  ResponseEntity<>(productServices.searchProducts(keyword), HttpStatus.OK);
    }
}
