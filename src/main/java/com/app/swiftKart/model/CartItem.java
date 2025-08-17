package com.app.swiftKart.model;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity(name="cart_table")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    @ManyToOne // one user can add multiple cart item
    @JoinColumn(name="user_id",nullable=false)
private UserEntity user;
    @ManyToOne  // one cart item has multiple products
    @JoinColumn(name="product_id",nullable = false)
private Product product;
private Integer quantity;
private BigDecimal price ;
@UpdateTimestamp
private LocalDateTime updatedAt;
@CreationTimestamp
private LocalDateTime createdAt;

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
