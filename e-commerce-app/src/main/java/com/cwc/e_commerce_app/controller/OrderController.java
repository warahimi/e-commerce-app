package com.cwc.e_commerce_app.controller;

import com.cwc.e_commerce_app.dto.OrderResponse;
import com.cwc.e_commerce_app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // Place order for a user
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestHeader("X-User-Id") Long userId) // user ID for whom the order is to be placed
    {
        OrderResponse orderResponse = orderService.placeOrder(userId);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
