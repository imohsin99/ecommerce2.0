package com.example.Dto;

import com.example.Models.Product;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserDto {
  private long user_id;
  private String name;
  private String user_name;
  private long cnic;
  private String email;
  private String password;
  private String path;
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

  public UserDto(String name, String user_name, long cnic, String email, String password, String path) {
    this.name = name;
    this.user_name = user_name;
    this.cnic = cnic;
    this.email = email;
    this.password = password;
    this.path = path;
  }

  public UserDto() {
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
