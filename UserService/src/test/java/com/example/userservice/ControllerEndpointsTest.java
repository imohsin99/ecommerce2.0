package com.example.userservice;

import com.example.Models.Product;
import com.example.Models.User;
import com.example.userservice.Controller.UserController;
import com.example.userservice.Helper.Mapper;
import com.example.userservice.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {UserServiceApplication.class})
public class ControllerEndpointsTest {
 private MockMvc mvc;
  private ObjectMapper objectMapper = new ObjectMapper();
  String url = "/api/user/";
  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private UserService userServiceMock;

  @BeforeEach
  public void setUp() {
    this.mvc = webAppContextSetup(webApplicationContext).build();
  }
  @Mock
  User user;
  @Mock
  Product product;

  @Test
  public void test_addUser() throws Exception {
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.addUser(user)).thenReturn(user);
    String jsonObj = objectMapper.writeValueAsString(user);
    mvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObj))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("Created"));
  }
  
  @Test
  public void test_getUsers() throws Exception {
    List<User> allUsers = new ArrayList<>();
    allUsers.add(new User("ali","ali",3520205889l,"ali@gmail.com","ali123",""));
    allUsers.add(new User("user","user",3520205999l,"user@gmail.com","user123",""));
    allUsers.add(new User("test","test",3520205889l,"test@gmail.com","test123",""));
    when(userServiceMock.getUsers()).thenReturn(allUsers.stream().map(user -> Mapper.UserToUserDto(user)).collect(Collectors.toList()));
    mvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(3));
  }

  @Test
  public void test_authenticate_no_user() throws Exception{
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.auth(user.getEmail(),user.getPassword())).thenReturn("no_user");

    mvc.perform(post(url+"auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("no_user"));
  }

  @Test
  public void test_authenticate_true() throws Exception{
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.auth(user.getEmail(),user.getPassword())).thenReturn("true");

    mvc.perform(post(url+"auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("true"));
  }


  @Test
  public void test_authenticate_false() throws Exception{
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.auth(user.getEmail(),user.getPassword())).thenReturn("false");

    mvc.perform(post(url+"auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("false"));
  }
  @Test
  public void test_getUserByEmail() throws Exception {
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.getUserByEmail(user.getEmail())).thenReturn(user);
    mvc.perform(get(url+"/getUserByEmail/"+user.getEmail())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  public void test_updateUser() throws Exception{
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.update(user.getUser_id(),user)).thenReturn(user);
    mvc.perform(put(url+"update/"+user.getUser_id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("user updated"));
  }
  @Test
  public void test_checkResource_true() throws Exception{
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.checkCnic(String.valueOf(user.getCnic()))).thenReturn(true);
    mvc.perform(get(url+"/check-cnic/"+user.getCnic())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").value(true));
  }
  @Test
    public void test_checkResource_false() throws Exception{
      user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
      when(userServiceMock.checkCnic(String.valueOf(user.getCnic()))).thenReturn(false);
      mvc.perform(get(url+"/check-cnic/"+user.getCnic())
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$").value(false));
    }
  @Test
  public void test_checkUsername_true() throws Exception{
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.checkUsername(user.getUser_name())).thenReturn(true);
    mvc.perform(get(url+"/check-username/"+user.getUser_name())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").value(true));
  }

  @Test
  public void test_checkUsername_false() throws Exception{
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.checkUsername(user.getUser_name())).thenReturn(false);
    mvc.perform(get(url+"/check-username/"+user.getUser_name())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").value(false));
  }

  @Test
  public void test_checkEmail_true() throws Exception{
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.checkEmail(user.getEmail())).thenReturn(true);
    mvc.perform(get(url+"/check-email/"+user.getEmail())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").value(true));
  }

  @Test
  public void test_checkEmail_false() throws Exception{
    user = new User("ali", "alimajeed", 3520205889l, "ali@gmail.com", "ali123", "");
    when(userServiceMock.checkUsername(user.getEmail())).thenReturn(false);
    mvc.perform(get(url+"/check-email/"+user.getUser_name())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").value(false));
  }

}
