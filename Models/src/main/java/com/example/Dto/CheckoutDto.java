package com.example.Dto;

import com.example.Models.Cart;
import com.example.Models.User;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckoutDto {
  private Long checkout_id;
  private String status;
  Cart cart;
  User user;

  public CheckoutDto() {
  }

  public CheckoutDto(Long checkout_id, String status, Cart cart, User user) {
    this.checkout_id = checkout_id;
    this.status = status;
    this.cart = cart;
    this.user = user;
  }

  public Long getCheckout_id() {
    return checkout_id;
  }

  public void setCheckout_id(Long checkout_id) {
    this.checkout_id = checkout_id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "CheckoutDto{" +
        "checkout_id=" + checkout_id +
        ", status='" + status + '\'' +
        ", cart=" + cart +
        ", user=" + user +
        '}';
  }
}
