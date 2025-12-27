package com.cwc.order_service.entities;

import com.cwc.order_service.entities.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// represent individual items within an order
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@ManyToOne // Many order items can belong to one product, one product can be in many OrderItems.
                // And one product can be in many orders.
    //@JoinColumn(name = "product_id", nullable = false) // product_id is Foreign key to Product entity
    //private Product product;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    @ManyToOne // Many order items can belong to one order, one order can have many OrderItems.
    @JoinColumn(name = "order_id", nullable = false) // order_id is Foreign key to Order entity
    private Order order; // which order this item belongs to
}
