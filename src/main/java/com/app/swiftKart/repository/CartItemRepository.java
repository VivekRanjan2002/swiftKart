package com.app.swiftKart.repository;

import com.app.swiftKart.model.CartItem;
import com.app.swiftKart.model.Product;
import com.app.swiftKart.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByUserAndProduct(UserEntity user, Product product );

    void deleteByUserAndProduct(UserEntity user, Product product);

    List<CartItem> findByUser(UserEntity user);

    void deleteByUser(UserEntity user);
}
