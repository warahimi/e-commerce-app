package com.cwc.product_service.controllers;

import com.cwc.product_service.dto.ProductRequest;
import com.cwc.product_service.dto.ProductResponse;
import com.cwc.product_service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // Create Product
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse savedProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(201).body(savedProduct);
    }

    // get all products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.FOUND);
    }
    // get product by id
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        ProductResponse product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.FOUND);
    }

    // update Product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    // Active or Deactivate Product
    @PatchMapping("/{productId}/active")
    public ResponseEntity<ProductResponse> activeProduct(@PathVariable Long productId, @RequestParam Boolean isActive) {
        ProductResponse updatedProduct = productService.activeProduct(productId, isActive);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete Product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean deleted = productService.deleteProduct(productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /* --------------------------------------------------------------------------------------------------*/
    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponse>> searchProductsInName(@RequestParam String keyword) {
        List<ProductResponse> products = productService.searchProductsInName(keyword);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/search/nameordescription")
    public ResponseEntity<List<ProductResponse>> searchProductsInNameOrDescription(@RequestParam String keyword) {
        List<ProductResponse> products = productService.searchProductsInNameOrDescription(keyword);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/search/2/name")
    public ResponseEntity<List<ProductResponse>> searchProductsInName2(@RequestParam String keyword) {
        List<ProductResponse> products = productService.searchProductsInName2(keyword);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/search/2/nameordescription")
    public ResponseEntity<List<ProductResponse>> searchProductsInNameOrDescription2(@RequestParam String keyword) {
        List<ProductResponse> products = productService.searchProductsInNameOrDescription2(keyword);
        return ResponseEntity.ok(products);
    }
    /* --------------------------------------------------------------------------------------------------*/
}
