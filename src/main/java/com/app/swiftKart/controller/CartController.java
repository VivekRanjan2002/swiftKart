package com.app.swiftKart.controller;

import com.app.swiftKart.dto.CartItemRequest;
import com.app.swiftKart.model.CartItem;
import com.app.swiftKart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private  CartService cartService;
    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request
    ){
        if(!cartService.addToCart(userId, request)){
            return ResponseEntity.badRequest().body("Product Out of Stock or User not found or Product not found");
        };
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId){
        boolean isDeleted=cartService.deleteItemFromCart(userId,productId);
    return isDeleted?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }
    @GetMapping
    public ResponseEntity<List<CartItem>> fetchCart(
            @RequestHeader("X-User-ID") String userId
    ){
         return ResponseEntity.ok(cartService.fetchCartByUserId(userId));

    }

}
