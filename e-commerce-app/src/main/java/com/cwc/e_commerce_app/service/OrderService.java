package com.cwc.e_commerce_app.service;

import com.cwc.e_commerce_app.Entity.*;
import com.cwc.e_commerce_app.dto.CartItemResponse;
import com.cwc.e_commerce_app.dto.OrderItemResponse;
import com.cwc.e_commerce_app.dto.OrderResponse;
import com.cwc.e_commerce_app.dto.UserResponse;
import com.cwc.e_commerce_app.repository.CartItemsRepository;
import com.cwc.e_commerce_app.repository.OrderRepository;
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
public class OrderService {
    private final CartItemsRepository cartItemsRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    public OrderResponse placeOrder(Long userId) {
        // logic to place order for the user

        // 1. validations (verify user exists, cart is not empty, product availability, etc.)

        // Validate for cart items, a user should have at least one item in the cart to place an order.
        List<CartItem> cartItems = cartItemsRepository.findByUserId(userId);
        if(cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order. Cart is empty for user id: " + userId);
        }
        // validate the userId
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        // calculate total amount
        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalAmount);
        // map the list of cart items to order items and set to order
        order.getOrderItems().addAll(
                cartItems.stream().map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                }).toList()
        );
        Order savedOrder = orderRepository.save(order);

        // clear user's cart after order is placed
        //cartItemsRepository.deleteByUserId(userId);
        return mapToOrderResponse(savedOrder);
    }
    private final OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderItems().stream().map(
                        orderItem -> new OrderItemResponse(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getProduct().getName(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                        )
                ).toList(),
                order.getCreatedAt()
        );
    }

}

