package com.example.Dto;

import com.example.Models.Product;
import com.example.Models.User;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
public class CartItemsDto {
  private Long cart_item_id;
  private int quantity;
  private Product product;
  private User user;

  public CartItemsDto() {
  }

  public CartItemsDto(Long cart_item_id, int quantity, Product product, User user) {
    this.cart_item_id = cart_item_id;
    this.quantity = quantity;
    this.product = product;
    this.user = user;
  }

  public Long getCart_item_id() {
    return cart_item_id;
  }

  public void setCart_item_id(Long cart_item_id) {
    this.cart_item_id = cart_item_id;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "CartItemsDto{" +
        "cart_item_id=" + cart_item_id +
        ", quantity=" + quantity +
        ", product=" + product +
        ", user=" + user +
        '}';
  }
}
