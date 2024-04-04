package com.example.productservice.Controller;


import com.example.Dto.ProductDto;
import com.example.Models.Product;
import com.example.Models.Comment;
import com.example.Models.User;
import com.example.productservice.Service.ProductService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Path;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    /**
     * This will return all the products from service
     * @return collection of Products in form of data transfer object
     */
    @GetMapping
    public List<ProductDto> getAll(){
        return productService.findAll();
    }

    /**
     * This will add product
     * @param product is object of product we want to add
     * @return string response if product is added successfully
     */
    @PostMapping
    public String addProduct(
        @RequestBody Product product
    ){
        productService.addProduct(product);
        return "Product added";
    }

    /**
     * This will check if sku is available for new object
     * @param sku is checked if it not already existed
     * @return boolean response based upon availability
     */
    @PostMapping("/check-sku")
    public boolean checkSku(
        @RequestBody String sku
    ){
        return productService.checkSku(sku);
    }

    /**
     * This will return product based upon id
     * @param id is identifier on which id will be fetched
     * @return product in form of data transfer object
     */
    @GetMapping("/get-product-id/{id}")
    public ProductDto getProductById(
        @PathVariable("id") Long id
    ){
        return productService.getProductById(id);
    }

    /**
     * This will update product based upon id
     * @param product is new object to replace previous
     * @param id is identifier for which instance need to update
     * @return string response upon successful update
     */
    @PutMapping("update-product/{productId}")
    public String updateProduct(
        @RequestBody ProductDto product,
        @PathVariable("productId") String id
    ){
        return productService.updateProduct(product,Long.valueOf(id));
    }

    /**
     * This will delete product with specific id from records
     * @param id is identifier for which product to delete
     * @return string response upon successful deletion
     */
    @DeleteMapping(value = "delete-product-by-id/{id}")
    public String deleteProduct(
        @PathVariable("id") Long id
    ){
        String response = productService.deleteProductById(id);
        return "Product Deleted";
    }

    /**
     * This will add comment to product
     * @param comment is comment that is attaching with product
     * @param id is id for product with which comment will be added
     * @return string response if comment added successfully
     */
    @PostMapping(value = "/add-comment-to-product/{productId}")
    public String addCommentToProduct(
        @RequestBody Comment comment,
        @PathVariable("productId") Long id
    ){
        productService.addCommentToProduct(id,comment);
        return "";
    }

    /**
     * This will delete comment from product
     * @param ids is combination of product id and comment id in form <product_id>-<comment_id>
     * @return string response based upon successful action
     */
    @GetMapping("/delete-comment-from-product/{ids}")
    public String deleteComment(
        @PathVariable("ids") String ids
    ){
        String[] id = ids.split("-");
        productService.deleteComment(id[0],id[1]);
        return "comment deleted";
    }

    /**
     * This will decrease quantity of product
     * @param pId is identifier for product which quantity need to decrement
     * @param quanity is int value that will be decremented from current quantity
     * @return string response based upon successful decrement
     */
    @GetMapping("decrement-quantity/{pId}/{quantity}")
    public String decrementQuantity(
        @PathVariable("pId") String pId,
        @PathVariable("quantity")  String quanity
    ){
        return productService.decrementQuantity(pId,quanity);
    }

}