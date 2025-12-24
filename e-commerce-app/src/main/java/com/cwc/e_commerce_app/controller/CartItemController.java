package com.cwc.e_commerce_app.controller;

import com.cwc.e_commerce_app.dto.CartItemRequest;
import com.cwc.e_commerce_app.dto.CartItemResponse;
import com.cwc.e_commerce_app.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
public class CartItemController {
    private final CartService cartService;

    // get cart items for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCartItemsByUserId(@PathVariable Long userId) {
        List<CartItemResponse> cartItemResponse = cartService.getCartItemsByUserId(userId);
        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }

    // get All cart items
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getAllCartItems() {
        List<CartItemResponse> cartItemResponses = cartService.getAllCartItems();
        return new ResponseEntity<>(cartItemResponses, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<CartItemResponse> addCartItem(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CartItemRequest cartItemRequest)
    {
        CartItemResponse cartItemResponse = cartService.addToCart(userId, cartItemRequest);
        return new ResponseEntity<>(cartItemResponse, HttpStatus.CREATED);
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteFromItemCart(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId)
    {
        boolean result = cartService.removeFromCart(userId, productId);
        return result ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // increment ItemCart quantity for a user
    @PutMapping("/{productId}/increment/{incrementBy}")
    public ResponseEntity<CartItemResponse> incrementCartItemQuantity(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId,
            @PathVariable Integer incrementBy)
    {
        CartItemResponse cartItemResponse = cartService.incrementCartItemQuantity(userId, productId, incrementBy);
        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }
    @PutMapping("/{productId}/increment")
    public ResponseEntity<CartItemResponse> incrementCartItemQuantityByOne(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId)
    {
        CartItemResponse cartItemResponse = cartService.incrementCartItemQuantity(userId, productId, 1);
        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }

    // decrement ItemCart quantity for a user
    @PutMapping("/{productId}/decrement/{decrementBy}")
    public ResponseEntity<CartItemResponse> decrementCartItemQuantity(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId,
            @PathVariable Integer decrementBy)
    {
        CartItemResponse cartItemResponse = cartService.decrementCartItemQuantity(userId, productId, decrementBy);
        if(cartItemResponse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }
    @PutMapping("/{productId}/decrement")
    public ResponseEntity<CartItemResponse> decrementCartItemQuantityByOne(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId)
    {
        CartItemResponse cartItemResponse = cartService.decrementCartItemQuantity(userId, productId, 1);
        if(cartItemResponse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }


}
