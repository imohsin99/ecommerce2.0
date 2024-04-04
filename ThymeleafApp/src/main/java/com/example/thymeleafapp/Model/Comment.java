package com.example.thymeleafapp.Model;


public class Comment {
    private long comment_id;

    private String description;

    private Product product;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public Comment(String description, Product product) {
        this.description = description;
        this.product = product;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment_id=" + comment_id +
                ", description='" + description + '\'' +
                ", product=" + product +
                ", user=" + user +
                '}';
    }

    public Comment() {
    }
}
