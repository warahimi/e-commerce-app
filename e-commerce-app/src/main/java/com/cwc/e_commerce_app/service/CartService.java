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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
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
    public boolean removeFromCart(Long userId, Long productId) {
        Optional<CartItem> byUserIdAndProductId = cartItemsRepository.findByUserIdAndProductId(userId, productId);
        if(byUserIdAndProductId.isPresent())
        {
            cartItemsRepository.deleteByUserIdAndProductId(userId, productId);
            return true;
        }
        else {
            return false;
        }
    }
    // Increment the quantity of an existing cart item
    public CartItemResponse incrementCartItemQuantity(Long userId, Long productId, Integer incrementBy) {
        Optional<CartItem> cartItemOpt = cartItemsRepository.findByUserIdAndProductId(userId, productId);
        if(cartItemOpt.isPresent()) {
            if(incrementBy == null)
            {
                incrementBy = 1;
            }
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + incrementBy);
            CartItem updatedCartItem = cartItemsRepository.save(cartItem);
            return mapToDto(updatedCartItem);
        } else {
            throw new RuntimeException("Cart item not found for user id: " + userId + " and product id: " + productId);
        }
    }

    // Decrement the quantity of an existing cart item and if quantity reaches zero, remove the item from cart.
    public CartItemResponse decrementCartItemQuantity(Long userId, Long productId, Integer decrementBy) {
        Optional<CartItem> cartItemOpt = cartItemsRepository.findByUserIdAndProductId(userId, productId);
        if(cartItemOpt.isPresent()) {
            if(decrementBy == null)
            {
                decrementBy = 1;
            }
            CartItem cartItem = cartItemOpt.get();
            int newQuantity = cartItem.getQuantity() - decrementBy;
            if(newQuantity > 0) {
                cartItem.setQuantity(newQuantity);
                CartItem updatedCartItem = cartItemsRepository.save(cartItem);
                return mapToDto(updatedCartItem);
            } else {
                // remove the item from cart
                cartItemsRepository.deleteByUserIdAndProductId(userId, productId);
                return null; // or throw an exception or return a specific response indicating removal
            }
        } else {
            throw new RuntimeException("Cart item not found for user id: " + userId + " and product id: " + productId);
        }
    }
    // fetch cart items for a user

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

    public List<CartItemResponse> getCartItemsByUserId(Long userId) {
        List<CartItem> cartItems = cartItemsRepository.findAll().stream()
                .filter(cartItem -> cartItem.getUser().getId().equals(userId))
                .toList();
        return cartItems.stream()
                .map(this::mapToDto)
                .toList();
    }
    // get all cart items
    public List<CartItemResponse> getAllCartItems() {
        List<CartItem> cartItems = cartItemsRepository.findAll();
        return cartItems.stream()
                .map(this::mapToDto)
                .toList();
    }
}
