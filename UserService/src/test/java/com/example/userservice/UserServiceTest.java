package com.example.userservice;

import com.example.Dto.UserDto;
import com.example.Models.User;
import com.example.userservice.Exceptions.ServiceException;
import com.example.userservice.Helper.Mapper;
import com.example.userservice.Repository.UserRepository;
import com.example.userservice.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {UserServiceTest.class,UserControllerTest.class, ControllerEndpointsTest.class})
public class UserServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  PasswordEncoder passwordEncoder;
  @Mock
  User user;
  @InjectMocks
  UserService userService;

  @Test
  public void test_getUsers(){
    List<User> allUsers = new ArrayList<>();
    allUsers.add(new User("ali","ali",3520205889l,"ali@gmail.com","ali123",""));
    allUsers.add(new User("user","user",3520205999l,"user@gmail.com","user123",""));
    allUsers.add(new User("test","test",3520205889l,"test@gmail.com","test123",""));
    when(userRepository.findAll()).thenReturn(allUsers);
    assertEquals(3,userService.getUsers().size());
  }


  @Test
  public void test_addUser(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userRepository.save(user)).thenReturn(user);
    assertEquals(user,userService.addUser(user));
   }


   @Test
  public void test_update(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    user.setUser_id(1);
    when(userRepository.save(user)).thenReturn(user);
    when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
    assertEquals(user,userService.update(1,user));
   }

   @Test
  public void test_auth_wrongPassword(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
    when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);
    assertEquals("wrong_password",userService.auth(user.getEmail(),"ali13"));

   }

  @Test
  public  void test_getUserByEmail() {
    List<User> allUsers = new ArrayList<>();
    allUsers.add(new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", ""));
    allUsers.add(new User("user", "user", 3520205999l, "user@gmail.com", "user123", ""));
    allUsers.add(new User("test", "test", 3520205889l, "test@gmail.com", "test123", ""));
    String email = "ali@gmail.com";
    when(userRepository.findUserByEmail(email)).thenReturn(allUsers.stream().filter(user -> user.getEmail().equals(email)).collect(Collectors.toList()).get(0));
    assertEquals(email, userService.getUserByEmail(email).getEmail());
  }

  @Test
  public void test_findById(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    user.setUser_id(1);
    when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
    assertEquals(user,userService.findById(1));
  }

  @Test
  public void test_checkCnic(){
    long cnic = 3520205889079l;
    user = new User("ali", "alimajeed", 3520205889079l, "ali@gmail.com", "ali123", "");
    when(userRepository.findByCnic(cnic)).thenReturn(user);
    assertEquals(false,userService.checkCnic(String.valueOf(user.getCnic())));
  }

  @Test
  public void test_checkUsername(){
    user = new User("ali", "alimajeed", 3520205889079l, "ali@gmail.com", "ali123", "");
    when(userRepository.findByUser_name(user.getName())).thenReturn(user);
    assertEquals(false,userService.checkUsername(user.getName()));
  }

  @Test
    public void test_checkEmail(){
      user = new User("ali", "alimajeed", 3520205889079l, "ali@gmail.com", "ali123", "");
      when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
      assertEquals(false,userService.checkEmail(user.getEmail()));
    }
// Testing for null values
  @Test
  public void test_nullInAddUser(){
    user = new User("ali", "", 3520205889l, "ali@gmail.com", "ali123", "");
    System.out.println(user.getProducts());
    when(userRepository.save(user)).thenReturn(user);
    assertThrows(ServiceException.class,
        ()->{
      userService.addUser(user);
    });
  }

  @Test
  public void test_nullUpdate(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    user.setUser_id(1);
    when(userRepository.save(user)).thenReturn(user);
    when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
    assertThrows(NullPointerException.class,
        ()->{
          userService.addUser(new User());
        });
  }

  @Test
  public void test_auth_no_user(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
    when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);
    assertEquals("no_user",userService.auth("",""));

  }

  @Test
  public void test_auth_true(){
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
    when(passwordEncoder.matches(user.getPassword(),"ali123")).thenReturn(true);
    when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);
    assertEquals("true",userService.auth(user.getEmail(),user.getPassword()));

  }

}
