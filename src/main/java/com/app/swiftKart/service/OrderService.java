package com.app.swiftKart.service;

import com.app.swiftKart.dto.OrderItemDTO;
import com.app.swiftKart.dto.OrderResponse;
import com.app.swiftKart.model.CartItem;
import com.app.swiftKart.model.Order;
import com.app.swiftKart.model.OrderItem;
import com.app.swiftKart.model.OrderStatus.OrderStatus;
import com.app.swiftKart.model.UserEntity;
import com.app.swiftKart.repository.OrderRepository;
import com.app.swiftKart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private  CartService cartService;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private   OrderRepository orderRepository;
    public Optional<OrderResponse> createOrder(String userId){
        // validate for user
        Optional<UserEntity> userOptional=userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()){
            return Optional.empty();
        }
        UserEntity user =userOptional.get();
        //validate for cart items
        List<CartItem> cartItems= cartService.fetchCartByUserId(userId);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }
        //calculate total price
        BigDecimal totalPrice= cartItems.stream()
                .map(CartItem:: getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        // create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems= cartItems.stream()
                .map(item-> new OrderItem(
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();
        order.setItems(orderItems);
        Order savedOrder= orderRepository.save(order);

        // clear the cart
        cartService.clearCart(userId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }
    private OrderResponse mapToOrderResponse(Order order){
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem->new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice()
                        )).toList(),
                order.getCreatedAt()
        );
    }
}
