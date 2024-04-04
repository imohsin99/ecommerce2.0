package com.example.userservice;

import com.example.Dto.UserDto;
import com.example.Models.Product;
import com.example.Models.User;
import com.example.userservice.Controller.UserController;
import com.example.userservice.Helper.Mapper;
import com.example.userservice.Service.UserService;
import org.hibernate.procedure.ProcedureOutputs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
public class UserControllerTest {

  @Mock
  WebClient.Builder webClientBuilder ;
  @Mock
  UserService userService;

  @Mock
  User user;
  @Mock
  Product product;
  @Mock
  WebClient webClient;

  @InjectMocks
  UserController userController;

  private WebClient.RequestBodyUriSpec requestBodyUriMock;
  private WebClient.RequestHeadersSpec requestHeadersMock;
  private WebClient.RequestBodySpec requestBodyMock;
  private WebClient.ResponseSpec responseMock;
  private WebClient webClientMock;

  @Test
  public void test_addUser(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.addUser(user)).thenReturn(user);
    assertEquals("Created",userController.addUser(Mapper.UserToUserDto(user)));
  }

  @Test
  public void test_addUserException(){
    user = null;
    assertThrows(NullPointerException.class,
        ()->{
          userController.addUser(Mapper.UserToUserDto(user));
        });
  }

  @Test
  public void test_getUsers(){
    List<User> allUsers = new ArrayList<>();
    allUsers.add(new User("ali","ali",3520205889l,"ali@gmail.com","ali123",""));
    allUsers.add(new User("user","user",3520205999l,"user@gmail.com","user123",""));
    allUsers.add(new User("test","test",3520205889l,"test@gmail.com","test123",""));
    when(userService.getUsers()).thenReturn(allUsers.stream().map(user -> Mapper.UserToUserDto(user)).collect(Collectors.toList()));
    assertEquals(3,userController.getUsers().size());
  }

  @Test
  public void test_updateUser(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    user.setUser_id(1);
    when(userService.update(1l,user)).thenReturn(user);
    assertEquals("user updated",userController.updateUser(Mapper.UserToUserDto(user),String.valueOf(user.getUser_id())));
  }
@Test
  public void test_authenticate_no_user(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.auth(user.getEmail(),user.getPassword())).thenReturn("no_user");
    assertEquals("no_user",userController.authenticate(user));
  }

  @Test
  public void test_authenticate_true(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.auth(user.getEmail(),user.getPassword())).thenReturn("true");
    assertEquals("true",userController.authenticate(user));
  }

  @Test
  public void test_authenticate_wrong_password(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.auth(user.getEmail(),user.getPassword())).thenReturn("wrong_password");
    assertEquals("wrong_password",userController.authenticate(user));
  }


  @Test
  public void test_getUserByEmail(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
    assertEquals(Mapper.UserToUserDto(user).getUser_name(),userController.getUserByEmail(user.getEmail()).getUser_name());
  }


  @Test
  public void test_checkResource_true(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.checkCnic(String.valueOf(user.getCnic()))).thenReturn(true);
    assertEquals(true,userController.checkResource(String.valueOf(user.getCnic())));
  }

  @Test
  public void test_checkResource_false(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.checkCnic(String.valueOf(user.getCnic()))).thenReturn(false);
    assertEquals(false,userController.checkResource(String.valueOf(user.getCnic())));
  }

  @Test
  public void test_checkUsername_true(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.checkUsername(user.getUser_name())).thenReturn(true);
    assertEquals(true,userController.checkUsername(user.getUser_name()));
  }

  @Test
  public void test_checkUsername_false(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.checkUsername(user.getUser_name())).thenReturn(false);
    assertEquals(false,userController.checkUsername(user.getUser_name()));
  }

  @Test
  public void test_checkEmail_false(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.checkEmail(user.getEmail())).thenReturn(false);
    assertEquals(false,userController.checkEmail(user.getUser_name()));
  }

  @Test
  public void test_checkEmail_true(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userService.checkUsername(user.getUser_name())).thenReturn(true);
    assertEquals(true,userController.checkUsername(user.getUser_name()));
  }

//  @BeforeEach
//  void mockWebClient() {
//    requestBodyUriMock = mock(WebClient.RequestBodyUriSpec.class);
//    requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
//    requestBodyMock = mock(WebClient.RequestBodySpec.class);
//    responseMock = mock(WebClient.ResponseSpec.class);
//    webClientMock = mock(WebClient.class);
//  }

//  @Test
//  public void test_addProductByID(){
//    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
//    product = new Product();
//    product.setName("test");product.setQuantity(20);product.setSku("test");
//
//    when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
//    when(webClientBuilder.build()).thenReturn(webClient);
//    when(webClient.post()).thenReturn(requestBodyUriMock);
//    when(requestBodyUriMock.uri("http://product-service/api/product")).thenReturn(requestBodyMock);
//    when(requestBodyMock.syncBody(product)).thenReturn(requestHeadersMock);
//    when(requestHeadersMock.retrieve()).thenReturn(responseMock);
//    when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just("added"));
//    assertEquals("product added",userController.addProductByID(product,user.getEmail()));
//
//  }


}
