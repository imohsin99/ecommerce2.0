package com.example.thymeleafapp.Controller;

import com.example.thymeleafapp.Model.*;
import com.example.thymeleafapp.Paypal.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;

@Controller
public class AppController {

   @Autowired
    WebClient.Builder webClientBuilder ;

    @Autowired
    private JavaMailSender javaMailSender;


    @GetMapping("/")
    public String showIndexPage(){
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpPage(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "sign-up";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user,Model model){
        if (user.getName().trim().equals("")){
            model.addAttribute("issue","name");
            return "sign-up";
        }
        try{
        if (user.getUser_name().trim().equals("")){
            model.addAttribute("issue","username");
            return "sign-up";
        }
        if (user.getCnic() == 0){
            model.addAttribute("issue","cnic");
            return "sign-up";
        }
        if (user.getEmail().trim().equals("")){
            model.addAttribute("issue","email");
            return "sign-up";
        }
        if (user.getPassword().trim().equals("")){
            model.addAttribute("issue","password");
            return "sign-up";
        }
        if (user.getPassword().length() < 5 ){
            model.addAttribute("issue","password_length");
            return "sign-up";
        }
        if (user.getPassword().matches("^[a-zA-Z0-9]")){
            model.addAttribute("issue","alphanumeric");
            return "sign-up";
        }
        if (String.valueOf(user.getCnic()).length()!=13){
            model.addAttribute("issue","CNICdigits");
            return "sign-up";
        }
        Boolean usernameCheck =  webClientBuilder.build().get()
            .uri("http://localhost:8080/api/user/check-username/"+user.getUser_name())
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();
        if(!usernameCheck){
            model.addAttribute("issue","Username_already_exists");
            return "sign-up";
        }

        Boolean cnicCheck =  webClientBuilder.build().get()
            .uri("http://localhost:8080/api/user/check-cnic/"+user.getCnic())
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();
        if(!cnicCheck){
            model.addAttribute("issue","Cnic_already_exists");
            return "sign-up";
        }
        Boolean emailCheck =  webClientBuilder.build().get()
            .uri("http://localhost:8080/api/user/check-email/"+user.getEmail())
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();
        if(!emailCheck){
            model.addAttribute("issue","Email_already_exists");
            return "sign-up";
        }
        String p1 =  webClientBuilder.build().post()
                .uri("http://localhost:8080/api/user")
                .syncBody(user)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "sign-in";
        } catch (Exception e) {
          model.addAttribute("issue",e.getMessage());
          return "sign-up";
        }
    }

    @GetMapping("/show-all-users")
    public String showAllUsers(Model model){
        User[] usersList = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/user")
                .retrieve()
                .bodyToMono(User[].class)
                .block();
        model.addAttribute("userList",usersList);
        return "show_all_users";
    }

    @GetMapping("/login")
    public String login(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "sign-in";
    }

    @PostMapping("/authenticate")
    public String authenticate(@ModelAttribute User user, HttpServletRequest request, Model model,HttpSession session){
        try {
          String response = webClientBuilder.build().post()
              .uri("http://localhost:8080/api/user/auth")
              .syncBody(user)
              .retrieve()
              .bodyToMono(String.class)
              .block();
          if (response.equals("true")) {
            User fetchedUser = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/user/getUserByEmail/" + user.getEmail())
                .retrieve()
                .bodyToMono(User.class)
                .block();
            saveUserSession(fetchedUser, request);
            return showProduct(model, user, session);
          } else {
            model.addAttribute("auth", response);
            return "sign-in";
          }
        } catch (Exception e) {
          model.addAttribute("auth", e.getLocalizedMessage());
          return "sign-in";
        }
    }
    @GetMapping("/products")
    public String showProduct(Model model,User user,HttpSession session) {
      if (session.getAttribute("userEmail") == null) {
        return "index";
      }
      try {
        Product[] products = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/product")
            .retrieve()
            .bodyToMono(Product[].class)
            .block();
      List<Product> productsByOtherUser = Arrays.stream(products)
          .filter(product -> !product.getUser().getEmail().equals(session.getAttribute("userEmail")))
          .collect(Collectors.toList());
      model.addAttribute("products", productsByOtherUser);
      return "show-products";
    } catch (Exception e) {
        model.addAttribute("issue",e.getMessage());
        return "show-products";
      }
    }

    public User findUserByEmail(String email){
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/user/getUserByEmail/"+email)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }
    @GetMapping("/show-products")
     public String products(){
        return "show-products";
    }

    @GetMapping("/show-current-user-products")
    public String showUserProducts(){
        return "redirect:/myproducts";
    }



    private void saveUserSession(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("username",user.getUser_name());
        session.setAttribute("userEmail",user.getEmail());
        session.setAttribute("userId",user.getUser_id());
        session.setAttribute("userProfileImage",user.getPath());
    }

    @GetMapping("add-product-page")
    public String addProduct(HttpSession session,Model model){
        if(session.getAttribute("userEmail") == null){
            return "index";
        }
       Product product = new Product();
        model.addAttribute("product",product);
        MultipartFile[] files= null;
        model.addAttribute("files",files);
        model.addAttribute("issue","");
        return "product-registration";
    }

    @PostMapping(value = "save-product" )
    public String saveProduct(
            @ModelAttribute Product product,
            Model model,
            HttpSession session,
            @RequestParam MultipartFile[] files
    ) throws IOException {
        if(session.getAttribute("userEmail") == null){
            return "index";
        }
        List<String> issue = new ArrayList<>();
        if (product.getName().trim().equals("")){
            issue.add("name");
        }
         if (product.getSku().trim().equals("")){
             issue.add("SKU");
        }
         if (product.getDescription().trim().equals("")){
             issue.add("Description");
        }
         if (product.getPrice()== 0 ){
             issue.add("Price");
        }
        if (product.getPrice()< 0){
            issue.add("PriceNegative");
        }
        if(product.getQuantity()< 1){
            issue.add("quantityZero");
        }
        if(files[0].isEmpty()){
            issue.add("image");
        }
        System.out.println(issue);
        if(!issue.isEmpty()){
            model.addAttribute("issue",issue);
            return "product-registration";
        }
        try {
          Boolean skuCheck = webClientBuilder.build().post()
              .uri("http://localhost:8080/api/product/check-sku")
              .syncBody(product.getSku())
              .retrieve()
              .bodyToMono(Boolean.class)
              .block();

          if (!skuCheck) {
            issue.add("sku_already");
            model.addAttribute("issue", issue);
            return "product-registration";
          }
          if (!files[0].isEmpty()) {
            List<String> images = ImageUploader.uploadProductImages(files);
            String product_images = "";
            for (String img : images) {
              product_images = product_images + img + "#";
            }
            product_images = product_images.substring(0, product_images.length() - 1);
            product.setImage(product_images);
            System.out.println(product_images);
          }
          saveProductAndInventory(product, session);
          return "redirect:/myproducts";
        } catch (Exception e) {
          model.addAttribute("issue",e.getLocalizedMessage());
          return "product-registration";
        }
    }

    private void saveProductAndInventory(Product product,HttpSession session) {
        Product targetProduct = new Product();
        targetProduct.setName(product.getName());
        targetProduct.setSku(product.getSku());
        targetProduct.setDescription(product.getDescription());
        targetProduct.setPrice(product.getPrice());
        User user = new User();
        String user_id = session.getAttribute("userId")+"";
        user.setUser_id(Long.valueOf(user_id));
        user.setEmail(session.getAttribute("userEmail")+"");
        user.setUser_name(session.getAttribute("username")+"");
        targetProduct.setUser(user);
        targetProduct.setQuantity(product.getQuantity());
        if(product.getImage() != null){targetProduct.setImage(product.getImage());}
        String addProduct =  webClientBuilder.build().post()
                .uri("http://localhost:8080/api/product/")
                .syncBody(targetProduct)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    @GetMapping(value = "myproducts")
    public String showMyProducts(Model model,HttpSession session){
        if(session.getAttribute("userEmail") == null){
            return "index";
        }
        try {
          Product[] products = webClientBuilder.build()
              .get()
              .uri("http://localhost:8080/api/product")
              .retrieve()
              .bodyToMono(Product[].class)
              .block();
          List<Product> currentUserProducts = Arrays.stream(products)
              .filter(product -> product.getUser().getEmail().equals(session.getAttribute("userEmail")))
              .collect(Collectors.toList());
          model.addAttribute("products", currentUserProducts);
          return "show-my-products";
        } catch (Exception e) {
          model.addAttribute("issue",e.getLocalizedMessage());
          return "show-my-products";
        }
    }

    @GetMapping("product/{id}")
    public String getProductById(
            @PathVariable("id") String id,
            Model model,
            HttpSession session
    ){
      try {
          if (session.getAttribute("userEmail") == null) {
            return "index";
          }
          Product product = getProductFromService(id);
          String[] imagesPath = product.getImage().split("#");
          model.addAttribute("product", product);
          model.addAttribute("images", imagesPath);
          model.addAttribute("userEmail", session.getAttribute("userEmail"));
          return "show-product-by-id";
      } catch (Exception e) {
        return  "redirect:/products";
      }
    }
    public Product getProductFromService(String id){
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/product/get-product-id/"+id)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }
    @GetMapping("/product/{id}/edit")
    public String editProduct(
            @PathVariable("id") String id,
            Model model,
            HttpSession session
    ){
        if(session.getAttribute("userEmail") == null){
            return "index";
        }
        Product currentProduct = getProductFromService(id);
        model.addAttribute("product",currentProduct);
        model.addAttribute("issue","");
        return "edit-product";
    }

    @PostMapping("product/{productId}/update")
    public String updateProduct(
            @ModelAttribute Product product,
            @PathVariable("productId") String id,
            @RequestParam MultipartFile[] files,
            HttpSession session,
            Model model
    )  {
      try {

        if (session.getAttribute("userEmail") == null) {
          return "index";
        }
        List<String> issue = new ArrayList<>();
        if (product.getName().trim().equals("")) {
          issue.add("name");
        }
        if (product.getSku().trim().equals("")) {
          issue.add("SKU");
        }
        if (product.getDescription().trim().equals("")) {
          issue.add("Description");
        }
        if (product.getPrice() == 0) {
          issue.add("Price");
        }
        if (product.getPrice() < 0) {
          issue.add("PriceNegative");
        }
        if (product.getQuantity() < 1) {
          issue.add("quantityZero");
        }
        if (files[0].isEmpty()) {
          issue.add("image");
        }
        if (!issue.isEmpty()) {
          model.addAttribute("issue", issue);
          return "edit-product";
        }
        if (files != null) {
          List<String> images = ImageUploader.uploadProductImages(files);
          String product_images = "";
          for (String img : images) {
            product_images = product_images + img + "#";
          }
          product_images = product_images.substring(0, product_images.length() - 1);
          product.setImage(product_images);
        } else {
          product.setImage(product.getImage());
        }
        String updateProductResponse = webClientBuilder.build().put()
            .uri("http://localhost:8080/api/product/update-product/" + id)
            .syncBody(product)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return "redirect:/myproducts";
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return "redirect://open-product-by-id/"+id;
      }
    }

    @GetMapping("/delete-product-by-id/{id}")
    public String deleteProductById(
            @PathVariable("id") String id,
            HttpSession session,
            Model model
    ){

        if(session.getAttribute("userEmail") == null){
            return "index";
        }
        try {
            Cart[] carts = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/checkout/")
                .retrieve()
                .bodyToMono(Cart[].class)
                .block();
            for (Cart cart : carts) {
                if (cart.getCartItems() != null) {
                    for (CartItems cartItem : cart.getCartItems()) {
                        if (cartItem.getProduct().getProduct_id() == Long.valueOf(id)) {
                            model.addAttribute("issue", "delete");
                            return "show-my-products";
                        }
                    }
                }
            }
        String deleteProduct = webClientBuilder.build().delete()
                .uri("http://localhost:8080/api/product/delete-product-by-id/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return "redirect:/myproducts";
        }
        catch (Exception e){
            model.addAttribute("issue","delete");
            return "show-my-products";
        }
    }

    @GetMapping("/product/comment/{productId}")
    public String addCommentToProduct(
            @PathVariable("productId") String id,
            Model model,
            HttpSession session
    ){
        if(session.getAttribute("userEmail") == null){
            return "index";
        }
        Comment comment = new Comment();
        model.addAttribute("comment",comment);
        model.addAttribute("productId",id);
        return "add-comment";
    }

    @PostMapping("save-comment/{pId}")
    public String saveComment(
            @ModelAttribute Comment comment,
            @PathVariable("pId") Long id,
            Model model,
            HttpSession session
    ){
      try {
        if (session.getAttribute("userEmail") == null) {
          return "index";
        }
        if (comment.getDescription().trim().equals("")) {
          model.addAttribute("productId", id);
          model.addAttribute("issue", "Comment can not be null");
          return "add-comment";
        }
        User user = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/user/getUserByEmail/" + session.getAttribute("userEmail"))
            .retrieve()
            .bodyToMono(User.class)
            .block();
        comment.setProduct(null);
        comment.setUser(user);
        String addCommentResponse = webClientBuilder.build().post()
            .uri("http://localhost:8080/api/product/add-comment-to-product/" + id)
            .syncBody(comment)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return "redirect:/product/" + id;
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return "redirect:/product/" + id;
      }
    }
    @GetMapping("/product/{pId}/comment/{cId}/edit")
    public String editComment(
            @PathVariable("cId") String id,
            @PathVariable("pId") String pId,
            Model model,
            HttpSession session
    ){
      try {
        if(session.getAttribute("userEmail") == null){
            return "index";
        }
        Comment comment =  webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/comment/"+id)
                .retrieve()
                .bodyToMono(Comment.class)
                .block();
        model.addAttribute("commentId",id);
        model.addAttribute("productId",pId);
        model.addAttribute("comment",comment);
        return "edit-comment";
    } catch (Exception e) {
        System.out.println(e.getLocalizedMessage());
        return "redirect:/product/"+pId;
      }
    }

    @PostMapping("/save-edited-comment/{commentId}/{pId}")
    public String saveEditedComment(
            @ModelAttribute Comment comment,
            Model model,
            @PathVariable("commentId") String id,
            @PathVariable("pId") String pId
    ){
        try{
        if(comment.getDescription().trim().equals("")){
            model.addAttribute("issue","Comment can not be null");
            model.addAttribute("commentId",id);
            model.addAttribute("productId",pId);
            return "edit-comment";
        }
        else {
            String saveEditComment =  webClientBuilder.build().put()
                    .uri("http://localhost:8080/api/comment/updateComment/"+id)
                    .syncBody(comment)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return "redirect:/product/"+pId;
        }} catch (Exception e) {
          System.out.println(e.getMessage());
          return "redirect:/product/"+pId;
        }
    }

    @GetMapping("/delete-comment/{id}")
    public String deleteComment(
            @PathVariable("id") String id,
            Model model,
            HttpSession session
    ){
      try {
        String deleteComment =  webClientBuilder.build().delete()
                .uri("http://localhost:8080/api/comment/"+id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return showProduct(model,findUserByEmail(session.getAttribute("userEmail").toString()),session);
      } catch (Exception e) {
        System.out.println(e.getLocalizedMessage());
        return showProduct(model,findUserByEmail(session.getAttribute("userEmail").toString()),session);
      }
    }
    @GetMapping("/open-product-by-id/{productId}")
    public String openProductById(
            @PathVariable("productId") String id,
            Model model
    ){
        Product product = getProductFromService(id);
        String[] images = new String[0];
        if(product.getImage()!= null) { images = product.getImage().split("#");}
        model.addAttribute("product",product);
        model.addAttribute("images",images);
        return "open-product-by-id";
    }

    @GetMapping("/add-product-to-cart/{productId}")
    public String addProductToCart(
            @PathVariable("productId") String pId,
            HttpSession session
    ){
        if(session.getAttribute("userEmail") == null){
            return "index";
        }
        Product product = getProductFromService(pId);
        CartItems cartItems = new CartItems();
        cartItems.setProduct(product);
        User user = findUserByEmail((String) session.getAttribute("userEmail"));
        cartItems.setUser(user);
        try {
            String saveCart = webClientBuilder.build().post()
                    .uri("http://localhost:8080/api/cart/")
                    .syncBody(cartItems)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return "redirect:/cart";

        }
        catch (Exception e){
            return "redirect:/cart";

        }

    }

    @GetMapping("/cart")
    public String getCart (
        Model model,
        HttpSession session
    ){
        if(session.getAttribute("userEmail") == null){
            return "index";
        }
       try {
            AtomicReference<Float> totalAmount = new AtomicReference<>((float) 0);
             Cart cartOfUser = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/api/cart/get-cart-by-user/" + session.getAttribute("userId"))
                    .retrieve()
                    .bodyToMono(Cart.class)
                    .block();
            List<CartItems> cartItemsListToAdd = new ArrayList<>();
            String cartId = String.valueOf(cartOfUser.getCart_id());
           if(cartOfUser != null){
                cartOfUser.getCartItems().forEach(cartItems -> {
                   totalAmount.set(totalAmount.get() + cartItems.getProduct().getPrice() * cartItems.getQuantity());
               });
           }else{
               cartOfUser = new Cart();
               cartOfUser.setCartItems(new ArrayList<CartItems>());
           }
           System.out.println(cartOfUser.toString());
            model.addAttribute("carts", cartOfUser);
            model.addAttribute("totalAmount", totalAmount);
            return "my-cart";
        }
       catch (Exception e){
           System.out.println(e.getMessage());
           return "my-cart";
       }
    }
    @GetMapping("increment-cart-quantity/{productId}/{userId}")
    public String incrementCartQuantity(
            @PathVariable("productId") String productId,
            @PathVariable("userId") String userId
    ){
        try{
          String incrementCartResponse =  webClientBuilder.build()
              .get()
              .uri("http://localhost:8080/api/cart/increment-cart-quantity/"+productId+"/"+userId)
              .retrieve()
              .bodyToMono(String.class)
              .block();
        } catch (Exception e) {
          System.out.println(e.getLocalizedMessage());
          return "redirect:/cart";
        }
        return "redirect:/cart";
    }
    @GetMapping("/checkouts")
    public String checkouts(
        Model model,
        HttpSession session
    ){
      try {
        if (session.getAttribute("userEmail") == null) {
          return "index";
        }
        User user = findUserByEmail(session.getAttribute("userEmail").toString());
        Checkout[] response = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/checkout/checkouts-by-user/" + user.getUser_id())
            .retrieve()
            .bodyToMono(Checkout[].class)
            .block();
        model.addAttribute("checkouts", response);
        return "checkouts";
      }catch (Exception e){
        System.out.println(e.getLocalizedMessage());
        return "checkouts";
      }
    }

    @GetMapping("decrement-cart-quantity/{productId}/{userId}")
    public String decrementCartQuantity(
            @PathVariable("productId") String productId,
            @PathVariable("userId") String userId
    ){
        String decrementCartResponse =  webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/cart/decrement-cart-quantity/"+productId+"/"+userId  )
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return "redirect:/cart";
    }
    @GetMapping("remove-from-cart/{cartId}/{cartItemId}")
    public String removeCart(
            @PathVariable("cartId") String cartId,
            @PathVariable("cartItemId") String cartItemId) {
        Cart cart = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/cart/" + cartId)
            .retrieve()
            .bodyToMono(Cart.class)
            .block();
        String response = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/cart/remove-from-cart/" + cartId+"/"+cartItemId)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return "redirect:/cart";
    }
    @Autowired
    PaypalService paypalService;
    @GetMapping("add-to-checkout/{cartId}")
    public String addToCheckouts(
        @PathVariable("cartId") String cartId,
        HttpSession session,
        @RequestParam("amount") Double amount,
        HttpServletRequest request
        ) throws PayPalRESTException {
        try {
          if (session.getAttribute("userEmail") == null) {
            return "index";
          }
          User user = findUserByEmail(session.getAttribute("userEmail").toString());
          Checkout checkout = new Checkout();
          checkout.setUser(user);
          List<Cart> carts = new ArrayList<>();
          Cart fetchedCart = webClientBuilder.build()
              .get()
              .uri("http://localhost:8080/api/cart/" + cartId)
              .retrieve()
              .bodyToMono(Cart.class)
              .block();
          checkout.setCart(fetchedCart);
          checkout.setStatus("Payment Not Verified");
          Checkout reponse = webClientBuilder.build().post()
              .uri("http://localhost:8080/api/checkout/")
              .syncBody(checkout)
              .retrieve()
              .bodyToMono(Checkout.class)
              .block();

          String cartResponse = webClientBuilder.build()
              .get()
              .uri("http://localhost:8080/api/cart/clear/" + cartId)
              .retrieve()
              .bodyToMono(String.class)
              .block();
          String[] urlParts = request.getRequestURL().toString().split("/");
          String url = urlParts[0] + "//" + urlParts[1] + urlParts[2];
          Payment payment = paypalService.createPayment(amount, "USD", "Paypal", "Sale", "Desc",
              url + "/checkouts", url + "/success-checkout");
          for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
              return "redirect:" + link.getHref();
            }
          }
        } catch (PayPalRESTException e) {
          System.out.println(e.getLocalizedMessage());
          return "redirect:/checkouts";
        }
      return "redirect:/checkouts";
    }

    @GetMapping("/success-checkout")
    public String successCheckout(
        HttpSession session
    ){
      try {
        User user = findUserByEmail(session.getAttribute("userEmail").toString());
        Checkout[] response = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/checkout/checkouts-by-user/" + user.getUser_id())
            .retrieve()
            .bodyToMono(Checkout[].class)
            .block();
        Long latestId = Arrays.stream(response)
            .mapToLong(checkout -> checkout.getCheckout_id())
            .max().orElseThrow(NoSuchElementException::new);
        String checkoutResponse = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/checkout/change-status/" + latestId)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        Checkout checkout = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/checkout/get-checkout-by-id/" + latestId)
            .retrieve()
            .bodyToMono(Checkout.class)
            .block();

        checkout.getCart().getCartItems().forEach(cartItems -> {
          String decrementResponse = webClientBuilder.build()
              .get()
              .uri("http://localhost:8080/api/product/decrement-quantity/" +
                  cartItems.getProduct().getProduct_id() + "/" + cartItems.getQuantity())
              .retrieve()
              .bodyToMono(String.class)
              .block();
        });
        return "redirect:/checkouts";
      }catch (Exception e){
        return "redirect:/checkouts";
      }
    }

    @GetMapping("profile")
    public String openProfile(
            HttpSession session,
            Model model
    ){
        User user = findUserByEmail(session.getAttribute("userEmail").toString());
        MultipartFile file = null;
        model.addAttribute("user",user);
        model.addAttribute("file",file);
        model.addAttribute("issue","");
        return "open-profile";
    }

    @PostMapping("update-user")
    public String updateUser(
            @ModelAttribute User user,
            @RequestParam MultipartFile file,
            HttpSession session,
            Model model
            ) throws IOException {
      try {
        if (session.getAttribute("userEmail") == null) {
          return "index";
        }
        List<String> issue = new ArrayList<>();
        if (user.getName().trim().equals("")) {
          issue.add("name");
        }
        if (user.getUser_name().trim().equals("")) {
          issue.add("username");
        }
        if (user.getCnic() == 0) {
          issue.add("cnic");
        }
        if (user.getEmail().trim().equals("")) {
          issue.add("email");
        }
        if (user.getPassword().length() < 5) {
          issue.add("pass_length");
        }
        if (user.getPassword().matches("^[a-zA-Z0-9]")) {
          issue.add("alphanumeric");
        }

        if (issue.size() > 0) {
          model.addAttribute("issue", issue);
          return "open-profile";
        }
        if (!file.isEmpty()) {
          user.setPath(ImageUploader.upload_profile_image(file));
        }
        String reponse = webClientBuilder.build().put()
            .uri("http://localhost:8080/api/user/update/" + session.getAttribute("userId"))
            .syncBody(user)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return "redirect:/logout";
      } catch (IOException e) {
        System.out.println(e.getMessage());
        return "redirect:/logout";
      }
    }

    @GetMapping("logout")
    public String logout(
            HttpSession session
    ){
        session.removeAttribute("username");
        session.removeAttribute("userEmail");
        session.removeAttribute("userId");
        return "index";
    }
    @GetMapping("search")
    public String showSearchedProducts(
             String keyword,
             HttpSession session,
             Model model
    ){
       Product[] products = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/product")
                .retrieve()
                .bodyToMono(Product[].class)
                .block();
        List<Product> productsByOtherUser = Arrays.stream(products)
                .filter(product -> !product.getUser().getEmail().equals(session.getAttribute("userEmail")))
                .collect(Collectors.toList());
        List<Product> searchedProduct= new ArrayList<>();

        for (Product prod : productsByOtherUser){
            if (prod.matchedString().split(keyword).length > 1){
                searchedProduct.add(prod);
            }
        }
        model.addAttribute("products", searchedProduct);
        return "show-products";
    }
    @GetMapping("/checkout/{checkout_id}")
    public String openCheckoutDetails(
        @PathVariable("checkout_id") String id,
        Model model,
        HttpSession session
    ){
        if(session.getAttribute("userEmail") == null){
            return "index";
        }
        Checkout checkout = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/checkout/get-checkout-by-id/"+id)
            .retrieve()
            .bodyToMono(Checkout.class)
            .block();
        model.addAttribute("checkoutCarts",checkout);
        return "checkout-details";
    }
    @GetMapping("delete-comment-from-product/{pId}/{cId}")
    public String deleteCommentFromProduct(
        @PathVariable("pId") String pId,
        @PathVariable("cId") String cId
    ){
        String response = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/product/delete-comment-from-product/"+pId+"-"+cId)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return "redirect:/product/"+pId;
    }

    @GetMapping("/forgetPassword")
    public String forgetPassword(
        Model model
    ){
        User user = new User();
        model.addAttribute("user",user);
        return "forget-password";
    }

    @PostMapping("/forget")
    public String forget(
        @ModelAttribute User user,
        Model model
    ){
        User targetUser = findUserByEmail(user.getEmail());
        try{
            if (targetUser.getEmail() == null) {
                model.addAttribute("issue", "No user exist. Kindly Signup!");
                return "forget-password";
            } else {
                if (targetUser.getCnic() == user.getCnic()) {
                    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                    simpleMailMessage.setFrom("e3476698@gmail.com");
                    simpleMailMessage.setTo(user.getEmail());
                    simpleMailMessage.setText("Hey " + user.getName() + ",\nYour Password is " + targetUser.getPassword());
                    simpleMailMessage.setSubject("Password Retreival");
                    javaMailSender.send(simpleMailMessage);
                    model.addAttribute("auth", "Email sent");
                    return "sign-in";
                } else {
                    model.addAttribute("issue", "Invalid Credentials!");
                    return "forget-password";
                }
            }


    } catch (MailException e) {
            System.out.println(e.getMessage());
            return "sign-in";
        }

    }
    @GetMapping("search/myproducts")
    public String showMySearchedProducts(
        String keyword,
        HttpSession session,
        Model model
    ){
        Product[] products = webClientBuilder.build()
            .get()
            .uri("http://localhost:8080/api/product")
            .retrieve()
            .bodyToMono(Product[].class)
            .block();
        List<Product> currentUserProducts = Arrays.stream(products)
            .filter(product -> product.getUser().getEmail().equals(session.getAttribute("userEmail")))
            .collect(Collectors.toList());
        List<Product> searchedProduct= new ArrayList<>();

        for (Product prod : currentUserProducts){
            if (prod.matchedString().split(keyword).length > 1){
                searchedProduct.add(prod);
            }
        }
        model.addAttribute("products", searchedProduct);
        return "show-my-products";
    }

}

