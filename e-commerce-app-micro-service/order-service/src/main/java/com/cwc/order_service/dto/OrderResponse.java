package com.cwc.order_service.dto;

import com.cwc.order_service.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;

    private List<OrderItemResponse> orderItems;

    private LocalDateTime createdAt;
}
