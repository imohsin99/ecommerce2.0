package com.example.Models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long cart_id;

  @OneToMany(cascade = CascadeType.MERGE)
  List<CartItems> cartItems;

  @OneToOne
  @JoinColumn(name = "user_id")
  User user;
  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createDateTime;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updateDateTime;

  private float amount;

  public Long getCart_id() {
    return cart_id;
  }

  public void setCart_id(Long cart_id) {
    this.cart_id = cart_id;
  }

  public List<CartItems> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItems> cartItems) {
    this.cartItems = cartItems;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Cart(Long cart_id, List<CartItems> cartItems, User user, float amount) {
    this.cart_id = cart_id;
    this.cartItems = cartItems;
    this.user = user;
    this.amount = amount;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public Cart() {
  }

  @Override
  public String toString() {
    return "Cart{" +
        "cart_id=" + cart_id +
        ", cartItems=" + cartItems +
        ", user=" + user +
        ", amount=" + amount +
        '}';
  }
}
