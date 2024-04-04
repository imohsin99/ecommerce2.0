package com.example.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "checkouts")
@AllArgsConstructor
public class Checkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkout_id;
    private String status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateDateTime;
    @OneToOne
    @JoinColumn(name = "cart_id")
    Cart cart;
    @ManyToOne(cascade = CascadeType.MERGE)
    User user;
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Checkout() {
    }

    public Checkout(String status, Cart cart, User user) {
        this.status = status;
        this.cart = cart;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Checkout{" +
            "checkout_id=" + checkout_id +
            ", status='" + status + '\'' +
            ", carts=" + cart +
            ", user=" + user +
            '}';}
}

