package com.cwc.product_service.services;

import com.cwc.product_service.entities.Product;
import com.cwc.product_service.repository.ProductRepository;
import com.cwc.product_service.dto.ProductRequest;
import com.cwc.product_service.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = mapToEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }
    // get all products
    public List<ProductResponse> getAllProducts() {
//        List<Product> products = productRepository.findAll();
//        return products.stream()
//                .filter(product -> product.getIsActive() != null && product.getIsActive())
//                .map(this::mapToResponse)
//                .toList();

        List<Product> products = productRepository.findByIsActiveTrue();
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    //get product by id
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        return mapToResponse(product);
    }

    //update product
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStockQuantity(productRequest.getStockQuantity());
        existingProduct.setCategory(productRequest.getCategory());
        existingProduct.setImageUrl(productRequest.getImageUrl());
        if (productRequest.getIsActive() != null) {
            existingProduct.setIsActive(productRequest.getIsActive());
        }
        Product updatedProduct = productRepository.save(existingProduct);
        return mapToResponse(updatedProduct);
    }

    // Active or Deactivate Product
    public ProductResponse activeProduct(Long productId, Boolean isActive) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        existingProduct.setIsActive(isActive);
        Product updatedProduct = productRepository.save(existingProduct);
        return mapToResponse(updatedProduct);
    }

    public boolean deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElse(null);
        if(existingProduct==null || existingProduct.getIsActive()==null || !existingProduct.getIsActive()) {
            return false;
        }
        existingProduct.setIsActive(false);
        productRepository.save(existingProduct);
        return true;
    }
    /* --------------------------------------------------------------------------------------------------*/
    public List<ProductResponse> searchProductsInName(String keyword)
    {
        List<Product> products = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword);
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }
    public List<ProductResponse> searchProductsInName2(String keyword) {
        return productRepository.searchByName(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // search by keyword either in name or description
    public List<ProductResponse> searchProductsInNameOrDescription(String keyword)
    {
        List<Product> products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndIsActiveTrue(keyword, keyword);
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }
    public List<ProductResponse> searchProductsInNameOrDescription2(String keyword) {
        return productRepository.searchByNameOrDescription(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    /* --------------------------------------------------------------------------------------------------*/

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
    private Product mapToEntity(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .category(productRequest.getCategory())
                .imageUrl(productRequest.getImageUrl())
                .isActive(productRequest.getIsActive() != null ? productRequest.getIsActive() : true)
                .build();
    }

}
