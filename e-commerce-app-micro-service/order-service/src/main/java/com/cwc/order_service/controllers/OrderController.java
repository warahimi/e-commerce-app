package com.cwc.order_service.controllers;

import com.cwc.order_service.dto.OrderResponse;
import com.cwc.order_service.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
