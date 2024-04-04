package com.example.productservice.Helper;

import com.example.Models.Product;
import com.example.Dto.ProductDto;

public class Mapper {
  public static ProductDto ProductToDto(Product product){
    ProductDto productDto = new ProductDto();
    productDto.setProduct_id(product.getProduct_id());
    productDto.setName(product.getName());
    productDto.setSku(product.getSku());
    productDto.setImage(product.getImage());
    productDto.setDescription(product.getDescription());
    productDto.setPrice(product.getPrice());
    productDto.setQuantity(product.getQuantity());
    productDto.setUser(product.getUser());
    productDto.setComments(product.getComments());
    return productDto;
  }
  public static Product DtoToProduct(ProductDto productDto){
    Product product = new Product();
    product.setProduct_id(productDto.getProduct_id());
    product.setName(productDto.getName());
    product.setSku(productDto.getSku());
    product.setImage(productDto.getImage());
    product.setDescription(productDto.getDescription());
    product.setPrice(productDto.getPrice());
    product.setQuantity(productDto.getQuantity());
    product.setUser(productDto.getUser());
    product.setComments(productDto.getComments());
    return product;
  }
}
