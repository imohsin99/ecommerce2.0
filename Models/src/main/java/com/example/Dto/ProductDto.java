package com.example.Dto;

import com.example.Models.Comment;
import com.example.Models.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
@Component
public class ProductDto {
        private  long product_id;
        private String name;
        private String sku;
        private String image;
        private String description;
        private BigDecimal price;
        private int quantity;
        private User user;
        private List<Comment> comments;

        public ProductDto() {
        }

        public ProductDto(long product_id, String name, String sku, String images, String description, BigDecimal price, int quantity, User user, List<Comment> comments) {
                this.product_id = product_id;
                this.name = name;
                this.sku = sku;
                this.image = images;
                this.description = description;
                this.price = price;
                this.quantity = quantity;
                this.user = user;
                this.comments = comments;
        }

        public long getProduct_id() {
                return product_id;
        }

        public void setProduct_id(long product_id) {
                this.product_id = product_id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getSku() {
                return sku;
        }

        public void setSku(String sku) {
                this.sku = sku;
        }

        public String getImage() {
                return image;
        }

        public void setImage(String images) {
                this.image = images;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public BigDecimal getPrice() {
                return price;
        }

        public void setPrice(BigDecimal price) {
                this.price = price;
        }

        public int getQuantity() {
                return quantity;
        }

        public void setQuantity(int quantity) {
                this.quantity = quantity;
        }

        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }

        public List<Comment> getComments() {
                return comments;
        }

        public void setComments(List<Comment> comments) {
                this.comments = comments;
        }

        @Override
        public String toString() {
                return "ProductDto{" +
                    "product_id=" + product_id +
                    ", name='" + name + '\'' +
                    ", sku='" + sku + '\'' +
                    ", images='" + image + '\'' +
                    ", description='" + description + '\'' +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    ", user=" + user +
                    ", comments=" + comments +
                    '}';
        }
}

