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
public class Product {
    private  long product_id;
    private String name;
    private String sku;
    private String image;
    private String description;
    private float price;

    private User user;

    private List<Comment> comments;

    private int quantity;

    @Override
    public String toString() {
        return "{" +
                "product_id=" + product_id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", images='" + image + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", user=" + user +
                ", comments=" + comments +
                ", quantity=" + quantity +
                '}';
    }
    public String matchedString(){
        return  product_id + name + sku + price + user.getUser_name()  ;
    }
}
