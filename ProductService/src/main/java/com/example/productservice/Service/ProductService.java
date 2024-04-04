package com.example.productservice.Service;


import com.example.Dto.ProductDto;
import com.example.Models.Comment;
import com.example.Models.Product;
import com.example.productservice.Exceptions.ServiceException;
import com.example.productservice.Helper.Mapper;
import com.example.productservice.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    /**
     * This will return all the products from service
     * @return collection of Products in form of data transfer object
     */
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream().map(Mapper::ProductToDto).collect(Collectors.toList());
    }
    /**
     * This will add product
     * @param product is object of product we want to add
     */
    public void addProduct(Product product) {
        if (product.getSku().trim().equals("") ||
            product.getName().trim().equals("")){
            throw new ServiceException("Null feilds not allowed");
        }
        productRepository.save(product);
    }
    /**
     * This will check if sku is available for new object
     * @param sku is checked if it not already existed
     * @return boolean response based upon availability
     */
    public boolean checkSku(String sku) {
        return productRepository.findBySku(sku) == null;
    }
    /**
     * This will return product based upon id
     * @param id is identifier on which id will be fetched
     * @return product in form of data transfer object
     */
    public ProductDto getProductById(Long id) {
        return Mapper.ProductToDto(productRepository.findById(id).get());
    }
    /**
     * This will update product based upon id
     * @param productDto is new product to replace previous in form of data transfer object
     * @param id is identifier for which instance need to update
     * @return string response upon successful update
     */
    public String updateProduct(ProductDto productDto,Long id){
        Product product = Mapper.DtoToProduct(productDto);
        if (product.getSku().trim().equals("") ||
            product.getName().trim().equals("") ||
            product == null){
            throw new ServiceException("Null feilds not allowed");
        }
        Product existingProduct = productRepository.findById(id).get();
        existingProduct.setName(product.getName());
        existingProduct.setSku(product.getSku());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        if(product.getImage()!= null){existingProduct.setImage(product.getImage());}
        productRepository.save(existingProduct);
        return "product updated";

    }
    /**
     * This will delete product with specific id from records
     * @param id is identifier for which product to delete
     * @return string response upon successful deletion
     */
    public String deleteProductById(Long id) {
        productRepository.deleteById(id);
        return "product deleted by service";
    }
    /**
     * This will add comment to product
     * @param comment is comment that is attaching with product
     * @param id is id for product with which comment will be added
     * @return string response if comment added successfully
     */
    public void addCommentToProduct(Long id, Comment comment){
        System.out.println(comment);
        Product product = productRepository.findById(id).get();
        List<Comment> existingComments = product.getComments();
        existingComments.add(comment);
        productRepository.save(product);
    }
    /**
     * This will delete comment from product
     * @param pId is product id from which comment will be deleted
     * @param cId is comment id which will be deleted
     */
    public void deleteComment(String pId,String cId) {
        Product product = productRepository.findById(Long.valueOf(pId)).get();
        List<Comment> comments = product.getComments();
        comments = comments.stream().filter(comment -> comment.getComment_id() != Long.parseLong(cId)).collect(Collectors.toList());
        product.setComments(comments);
        productRepository.save(product);
    }
    /**
     * This will decrease quantity of product
     * @param pId is identifier for product which quantity need to decrement
     * @param quanity is int value that will be decremented from current quantity
     * @return string response based upon successful decrement
     */
  public String decrementQuantity(String pId, String quanity) {
        Product product = productRepository.findById(Long.valueOf(pId)).get();
        product.setQuantity(product.getQuantity() - Integer.parseInt(quanity));
        productRepository.save(product);
        return "quantity changed";
  }
}