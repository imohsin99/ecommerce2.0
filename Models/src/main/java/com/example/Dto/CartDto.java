package com.example.Dto;

import com.example.Models.User;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CartDto {
  private Long cart_id;
  private List<CartItemsDto> products;
  private User user;
  private float amount;

  public CartDto() {
  }

  public CartDto(Long cart_id, List<CartItemsDto> products, User user, float amount) {
    this.cart_id = cart_id;
    this.products = products;
    this.user = user;
    this.amount = amount;
  }

  public Long getCart_id() {
    return cart_id;
  }

  public void setCart_id(Long cart_id) {
    this.cart_id = cart_id;
  }

  public List<CartItemsDto> getProducts() {
    return products;
  }

  public void setProducts(List<CartItemsDto> products) {
    this.products = products;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "CartDto{" +
        "cart_id=" + cart_id +
        ", products=" + products +
        ", user=" + user +
        ", amount=" + amount +
        '}';
  }
}
