package com.cwc.e_commerce_app.repository;

import com.cwc.e_commerce_app.Entity.CartItem;
import com.cwc.e_commerce_app.Entity.Product;
import com.cwc.e_commerce_app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserAndProduct(User user, Product product);
}
