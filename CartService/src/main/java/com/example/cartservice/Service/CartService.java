package com.example.cartservice.Service;

import com.example.Models.Cart;
import com.example.Models.CartItems;
import com.example.Models.User;
import com.example.cartservice.Repository.CartItemsRepository;
import com.example.cartservice.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemsRepository cartItemsRepository;

  /**
   * This will add cartItem to Cart. If cart is not existed than new Cart will be created and cartItem will be added.
   * If cart is already created than quantity will be increment for specific cartItem
   * @param cartItems is to be added in cart or incremented in existing cart
   * @return string response based upon alternative work done
   */
    public AtomicReference<String> addProductToCart(
            CartItems cartItems
    ){
        AtomicReference<String> c = new AtomicReference<>("success from cart service");
        Cart existingCart = cartRepository.findByUserId(cartItems.getUser().getUser_id());
        cartItems.setQuantity(1);
       if(existingCart == null){
           Cart cart = new Cart();
           cart.setUser(cartItems.getUser());
           CartItems cartItems1 = cartItemsRepository.save(cartItems);
           List<CartItems> cartItemsList = new ArrayList<>();
           cartItemsList.add(cartItems1);
           cart.setCartItems(cartItemsList);
           cart.setAmount((cartItems.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItems.getQuantity()))).floatValue());
           cartRepository.save(cart);
           c.set("cart saved");
           return c;
        }
        else{
           Cart cart = cartRepository.findCartByUser(cartItems.getUser());
           System.out.println("cart "+ cart.toString());
           List<CartItems> cartItemsList = cart.getCartItems();
           List<CartItems> listToUpdate = new ArrayList<>();
           if(cart.getCartItems().toString().contains(cartItems.getProduct().getSku())){
           cart.getCartItems().forEach(cartItems1 -> {
               if(cartItems1.getProduct().getSku().equals(cartItems.getProduct().getSku())){
                   CartItems updateItemList = cartItems1;
                   if(cartItems1.getQuantity()<cartItems1.getProduct().getQuantity()) {
                     updateItemList.setQuantity(cartItems1.getQuantity() + 1);
                   }
                   else{
                     updateItemList.setQuantity(cartItems1.getQuantity());
                   }
                   listToUpdate.add(updateItemList);
               }
               else {
                 listToUpdate.add(cartItems1);
               }
//
               cart.setCartItems(listToUpdate);
               cartRepository.save(cart);
           });
           }
           else {
                List<CartItems> cartItemsList1 = new ArrayList<>();
                cart.getCartItems().forEach(cartItems1 -> cartItemsList1.add(cartItems1));
                cartItems.setQuantity(1);
                CartItems cartItems1 = cartItemsRepository.save(cartItems);
                cartItemsList1.add(cartItems1);
                cart.setCartItems(cartItemsList1);
                cartRepository.save(cart);

           }
        }
        return c;
    }
  /**
   * This will return all the cart from database
   * @return cart in collection of list
   */
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }
  /**
   * This will return cart of specific user
   * @param user will identify cart of which user should return
   * @return cart of specific user
   */
    public Cart getCartsByUser(User user) {
        Cart cartsOfUser =  cartRepository.findCartByUser(user);
        if(cartsOfUser == null){
          return new Cart();
        }
        List<CartItems> toUpdateCartItems = new ArrayList<>();
        cartsOfUser.getCartItems().forEach(cartItems -> {
          if(cartItems.getProduct().getQuantity() == 0){

          }
          else if (cartItems.getQuantity() > cartItems.getProduct().getQuantity()){
            CartItems cartItems1 = new CartItems();
            cartItems1.setUser(cartItems.getUser());
            cartItems1.setProduct(cartItems.getProduct());
            cartItems1.setQuantity(cartItems.getProduct().getQuantity());
            toUpdateCartItems.add(cartItems1);
          }
          else {
            toUpdateCartItems.add(cartItems);
          }
        });
        cartsOfUser.setCartItems(toUpdateCartItems);
        cartRepository.save(cartsOfUser);
        System.out.println(cartsOfUser);
        return cartsOfUser;
    }

  /**
   * This will return cart by id
   * @param id identifies which cart will be return
   * @return cart based upon id
   */
    public Cart getCartById(String id){
      return cartRepository.findById(Long.valueOf(id)).get();
    }
  /**
   * This will increment cart quantity
   * @param productId identifies which product in cartItems of cart will be incremented
   * @param userId  identifies cart of which user need to increment cartItems quantity
   * @return string response if cart is incremented successfully
   */
    public String incrementQuantity(String productId, String userId){
        User user = new User();
        user.setUser_id(Long.valueOf(userId));
        Cart cart = cartRepository.findCartByUser(user);
        cart.getCartItems().forEach(cartItems -> {
            if (cartItems.getProduct().getProduct_id() == Long.valueOf(productId)){
                cartItems.setQuantity(cartItems.getQuantity()+1);
            }
        });
        cartRepository.save(cart);
        return "quantity incremented";
    }
  /**
   * This will decrement cart quantity
   * @param productId identifies which product in cartItems of cart will be decremented
   * @param userId  identifies cart of which user need to decrement cartItems quantity
   * @return string response if cart is decremented successfully
   */
    public String decrementQuantity(String productId, String userId) {
        User user = new User();
        user.setUser_id(Long.valueOf(userId));
        Cart cart = cartRepository.findCartByUser(user);
        cart.getCartItems().forEach(cartItems -> {
            if (cartItems.getProduct().getProduct_id() == Long.valueOf(productId)){
                cartItems.setQuantity(cartItems.getQuantity()-1);
            }
        });
        cartRepository.save(cart);
        return "quantity incremented";
    }
  /**
   * This will remove cartItem from cart
   * @param id identifies cartId from which cartItem will be removed
   * @param cartItemId identifies which cartItem to remove from cart
   * @return string response if cartItem is removed
   */
    public String removeFromCarts(String id,String cartItemId) {
      Cart cart = cartRepository.findById(Long.valueOf(id)).get();
      List<CartItems> cartItemsPresent = cart.getCartItems();
      List<CartItems> cartItemsToInsert = cartItemsPresent
          .stream()
          .filter(cartItems -> cartItems.getCart_item_id() != Long.valueOf(cartItemId))
          .collect(Collectors.toList());
      cart.setCartItems(cartItemsToInsert);
      cartRepository.save(cart);
        return "deleted from carts";
    }
  /**
   * This will clear the cart of
   * @param id will identify which cart will be cleared
   * @return string response if cart is cleared successfully
   */
    public void clear(String id) {
        Cart cart = cartRepository.findById(Long.valueOf(id)).get();
        cart.setUser(null);
        cartRepository.save(cart);

    }


}
