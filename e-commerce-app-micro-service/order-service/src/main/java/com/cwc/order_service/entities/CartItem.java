package com.cwc.order_service.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne // Many cart items can belong to one user, one user can add many CartItems.
//                // and one cart can have many items.
//    @JoinColumn(name = "user_id", nullable = false) // user_id is Foreign key to User entity
//    private User user; // The user who owns the cart item, a linked to User entity
//    @ManyToOne // Many cart items can belong to one product, one product can be in many CartItems.
//                // And one product can be in many carts.
//    @JoinColumn(name = "product_id", nullable = false) // product_id is Foreign key to Product entity
//    private Product product;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
