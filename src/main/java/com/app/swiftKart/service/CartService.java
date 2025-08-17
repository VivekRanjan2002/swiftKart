package com.app.swiftKart.service;

import com.app.swiftKart.dto.CartItemRequest;
import com.app.swiftKart.model.CartItem;
import com.app.swiftKart.model.Product;
import com.app.swiftKart.model.UserEntity;
import com.app.swiftKart.repository.CartItemRepository;
import com.app.swiftKart.repository.ProductRepository;
import com.app.swiftKart.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public CartService(ProductRepository productRepository,CartItemRepository cartItemRepository,UserRepository userRepository){
        this.productRepository=productRepository;
        this.userRepository=userRepository;
        this.cartItemRepository=cartItemRepository;
    }

    public boolean addToCart(String userId, CartItemRequest request){
        // look for product
              Optional<Product> productOpt=productRepository.findById(request.getProductId());
          if(productOpt.isEmpty()) return false;
        Product product= productOpt.get();
        if(product.getStockQuantity()<request.getQuantity()) return false;
        Optional<UserEntity> userOpt= userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) return false;
        UserEntity user= userOpt.get();
        CartItem existingCartItem= cartItemRepository.findByUserAndProduct(user,product);
        if(existingCartItem!=null){
            // update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity()+ request.getQuantity());
        existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
        cartItemRepository.save(existingCartItem);
        }
        else {
            // create a new cart item
            CartItem cartItem= new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);

        }
        return true;
    }
    public boolean deleteItemFromCart(String userId,Long productId){
        Optional<Product> productOpt= productRepository.findById(productId);
        Optional<UserEntity> userOpt= userRepository.findById(Long.valueOf(userId));
        if(userOpt.isPresent() && productOpt.isPresent()){
            cartItemRepository.deleteByUserAndProduct(userOpt.get(),productOpt.get());
            return true;
        }
        return false;
    }

    public List<CartItem> fetchCartByUserId(String userId) {
       return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);

    }
 @Transactional
    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId)).ifPresent(cartItemRepository::deleteByUser);
    }


}
