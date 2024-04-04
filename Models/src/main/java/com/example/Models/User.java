package com.example.Models;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.List;

@Entity
@Lazy(value = true)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    private String name;
    @Column(unique = true)
    private String user_name;
    @Column(unique = true)
    private long cnic;
    @Column(unique = true)
    private String email;
    @Column()
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", cnic=" + cnic +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", path='" + path + '\'' +
                ", products=" + products +
                '}';
    }

    private String path;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public User(String name, String user_name, long cnic, String email, String password, String path) {
        this.name = name;
        this.user_name = user_name;
        this.cnic = cnic;
        this.email = email;
        this.password = password;
        this.path = path;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getCnic() {
        return cnic;
    }

    public void setCnic(long cnic) {
        this.cnic = cnic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
