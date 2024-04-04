package com.example.thymeleafapp.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Checkout {
    private Long checkout_id;

    @Override
    public String toString() {
        return "Checkout{" +
            "checkout_id=" + checkout_id +
            ", status='" + status + '\'' +
            ", cart=" + cart +
            ", user=" + user +
            '}';
    }

    private String status;
    Cart cart;

    User user;


}

