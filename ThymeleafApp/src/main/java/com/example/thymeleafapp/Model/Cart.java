package com.example.thymeleafapp.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@RequiredArgsConstructor
@ToString
public class Cart {
  private Long cart_id;
  List<CartItems> cartItems;
  User user;
  private float amount;

}
