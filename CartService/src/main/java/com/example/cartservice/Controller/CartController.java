package com.example.cartservice.Controller;

import com.example.Models.Cart;
import com.example.Models.CartItems;
import com.example.Models.User;
import com.example.Dto.CartDto;
import com.example.Dto.CartItemsDto;
import com.example.cartservice.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartController {
    @Autowired
    CartService cartService;

    /**
     * This will add cartItemsDto in cart
     * @param cartItemsDto will be added to cart
     * @return string response if cartItemsDto is added successfully
     */
    @PostMapping
    public String addToCart(
        @RequestBody CartItems cartItemsDto
    ){
        cartService.addProductToCart(cartItemsDto);
        return "cart added";
    }

    /**
     * This will return all the cart from database
     * @return cart in collection of list
     */
    @GetMapping
    public List<Cart> showAllCarts(){
        return cartService.findAll();
    }

    /**
     * This will return cart of specific user
     * @param id will identify which user cart to return
     * @return
     */
    @GetMapping("/get-cart-by-user/{userId}")
    public Cart getCartByUser(
            @PathVariable("userId") String id
    ){
        User user = new User();
        user.setUser_id(Long.parseLong(id));
        return cartService.getCartsByUser(user);
    }

    /**
     * This will increment cart quantity
     * @param productId identifies which product in cartItems of cart will be incremented
     * @param userId  identifies cart of which user need to increment cartItems quantity
     * @return string response if cart is incremented successfully
     */
    @GetMapping("increment-cart-quantity/{productId}/{userId}")
    public String incrementCartQuantity(
        @PathVariable("productId") String productId,
        @PathVariable("userId") String userId
    ){
        return cartService.incrementQuantity(productId,userId);
    }
    /**
     * This will decrement cart quantity
     * @param productId identifies which product in cartItems of cart will be decremented
     * @param userId  identifies cart of which user need to decrement cartItems quantity
     * @return
     */
    @GetMapping("decrement-cart-quantity/{productId}/{userId}")
    public String decrementCartQuantity(
        @PathVariable("productId") String productId,
        @PathVariable("userId") String userId
    ){
        return cartService.decrementQuantity(productId,userId);
    }

    /**
     * This will remove cartItem from cart
     * @param id identifies cartId from which cartItem will be removed
     * @param cartItemId identifies which cartItem to remove from cart
     * @return string response if cartItem is removed
     */
    @GetMapping("remove-from-cart/{cartId}/{cartItemId}")
    public String removeFromCarts(
            @PathVariable("cartId") String id,
            @PathVariable("cartItemId") String cartItemId
    ){
        return cartService.removeFromCarts(id,cartItemId);
    }

    /**
     * This will return cart by id
     * @param id identifies which cart will be return
     * @return cart based upon id
     */
    @GetMapping("/{id}")
    public Cart getCardById(
            @PathVariable("id") String id
    ){
        return cartService.getCartById(id);
    }

    /**
     * This will clear the cart of
     * @param id will identify which cart will be cleared
     * @return string response if cart is cleared successfully
     */
    @GetMapping("clear/{id}")
    public String clear(
        @PathVariable("id") String id
    ){
        cartService.clear(id);
        return "cart cleared";
    }

}
