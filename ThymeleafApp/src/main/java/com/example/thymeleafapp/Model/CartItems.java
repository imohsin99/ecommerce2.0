package com.example.thymeleafapp.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CartItems {
    private Long cart_item_id;
    private int quantity;

    private Product product;

    private User user;

    @Override
    public String toString() {
        return "CartItem{" +
            "cart_item_id=" + cart_item_id +
            ", quantity=" + quantity +
            ", products=" + product +
            ", user=" + user +
            '}';
    }
}
