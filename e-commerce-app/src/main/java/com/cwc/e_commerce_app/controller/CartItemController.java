package com.cwc.e_commerce_app.controller;

import com.cwc.e_commerce_app.dto.CartItemRequest;
import com.cwc.e_commerce_app.dto.CartItemResponse;
import com.cwc.e_commerce_app.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
public class CartItemController {
    private final CartService cartService;
    @PostMapping
    public ResponseEntity<CartItemResponse> addCartItem(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CartItemRequest cartItemRequest)
    {
        CartItemResponse cartItemResponse = cartService.addToCart(userId, cartItemRequest);
        return new ResponseEntity<>(cartItemResponse, HttpStatus.CREATED);
    }
}
