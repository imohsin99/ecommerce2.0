package com.example.Dto;

import com.example.Models.Product;
import com.example.Models.User;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
public class CommentDto {
  private long comment_id;
  private String description;
  private Product product;
  private User user;

  public CommentDto() {
  }

  public CommentDto(long comment_id, String description, Product product, User user) {
    this.comment_id = comment_id;
    this.description = description;
    this.product = product;
    this.user = user;
  }

  public long getComment_id() {
    return comment_id;
  }

  public void setComment_id(long comment_id) {
    this.comment_id = comment_id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
    return "CommentDto{" +
        "comment_id=" + comment_id +
        ", description='" + description + '\'' +
        ", product=" + product +
        ", user=" + user +
        '}';
  }
}
