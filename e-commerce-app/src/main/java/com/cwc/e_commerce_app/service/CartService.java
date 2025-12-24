package com.cwc.e_commerce_app.service;

import com.cwc.e_commerce_app.Entity.CartItem;
import com.cwc.e_commerce_app.Entity.Product;
import com.cwc.e_commerce_app.Entity.User;
import com.cwc.e_commerce_app.dto.CartItemRequest;
import com.cwc.e_commerce_app.dto.CartItemResponse;
import com.cwc.e_commerce_app.repository.CartItemsRepository;
import com.cwc.e_commerce_app.repository.ProductRepository;
import com.cwc.e_commerce_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductRepository productRepository;
    private final CartItemsRepository cartItemsRepository;
    private final UserRepository userRepository;
    public CartItemResponse addToCart(Long userId, CartItemRequest cartItemRequest) {
        /* Perform Validations */
        // look for product by id
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + cartItemRequest.getProductId()));
        if(product.getStockQuantity() < cartItemRequest.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product id: " + cartItemRequest.getProductId());
        }

        // check for user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // everything is fine, proceed to add to cart

        // if the cart item for the user and product already exists, update the quantity

//        cartItemsRepository.findById(cartItemRequest.getProductId()).ifPresentOrElse(
//                existingCartItem -> {
//                    existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
//                    cartItemsRepository.save(existingCartItem);
//                },
//                () -> {
//                    // create new cart item
//                    var cartItem = CartItem.builder()
//                            .user(user)
//                            .product(product)
//                            .quantity(cartItemRequest.getQuantity())
//                            .price(product.getPrice().multiply(
//                                    BigDecimal.valueOf(cartItemRequest.getQuantity())
//                            ))
//                            .build();
//                    cartItemsRepository.save(cartItem);
//                }
//        );
        Optional<CartItem> carItem = cartItemsRepository.findByUserAndProduct(user, product);
        if(carItem.isPresent())
        {
            // update the quantity
            CartItem existingCartItem = carItem.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            cartItemsRepository.save(existingCartItem);
            return mapToDto(existingCartItem);
        }
        else {
            // create new cart item
            CartItem newCartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(cartItemRequest.getQuantity())
                    .price(product.getPrice().multiply(
                            BigDecimal.valueOf(cartItemRequest.getQuantity())
                    ))
                    .build();
            CartItem savedCartItem = cartItemsRepository.save(newCartItem);
            return mapToDto(savedCartItem);
        }
    }
    public void removeFromCart(Long userId, Long productId) {
        // check of user exist
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        // check if product exist
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));


    }
    private CartItemResponse mapToDto(CartItem cartItem) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .userId(cartItem.getUser().getId())
                .productId(cartItem.getProduct().getId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .build();
    }

}
