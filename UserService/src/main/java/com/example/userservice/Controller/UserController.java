package com.example.userservice.Controller;


import com.example.Models.Product;
import com.example.Models.User;
import com.example.Dto.UserDto;
import com.example.userservice.Exceptions.ServiceException;
import com.example.userservice.Helper.Mapper;
import com.example.userservice.Service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    WebClient.Builder webClientBuilder ;

  /**
   * This method simply add user
   * @param userDto is data transfer object for User Model
   * @return string response if User is created successfully
   */
    @PostMapping
    public String addUser(
            @RequestBody UserDto userDto
    ) {
        userService.addUser(Mapper.UserDtoToUser(userDto));
        return "Created";
    }

  /**
   * This method returns all the users present in our records
   * @return collection of user in form of data transfer objects
   */
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

  /**
   * This method authenticate the user based upon their credentials
   * @param user is instance of user with specific credentials
   * @return string response whether user is authenticated , not authenticated or user not exists
   */
    @PostMapping("/auth")
    public String authenticate(
            @RequestBody User user
    ){
       return userService.auth(user.getEmail(),user.getPassword());
    }

  /**
   * This method returns UserDto based upon email
   * @param email will identify user
   * @return UserDto
   */
    @GetMapping("/getUserByEmail/{email}")
    public UserDto getUserByEmail(
            @PathVariable("email") String email
    ){
        return Mapper.UserToUserDto(userService.getUserByEmail(email));
    }

  /**
   * This method update existing user with new user based upon id
   * @param user is new user that will replace the old user
   * @param id will identify the previous user object
   * @return response in string if user is updated successfully
   */
    @PutMapping(("/update/{userId}"))
    public String updateUser(
        @RequestBody UserDto user,
        @PathVariable("userId") String id
    ){
        userService.update(Long.parseLong(id),Mapper.UserDtoToUser(user));
        return "user updated";
    }

  /**
   * This will return whether the cnic is already assigned or not
   * @param cnic to check cnic that user want to register with
   * @return boolean response
   */
    @GetMapping("/check-cnic/{cnic}")
    public boolean checkResource(
        @PathVariable("cnic") String cnic
    ){
        return userService.checkCnic(cnic);
    }

  /**
   * This will return whether the username is already assigned or not
   * @param username to check username that user want to register with
   * @return boolean response
   */
    @GetMapping("/check-username/{username}")
    public boolean checkUsername(
        @PathVariable("username") String username
    ){
        return userService.checkUsername(username);
    }
  /**
   * This will return whether the email is already assigned or not
   * @param email to check email that user want to register with
   * @return boolean response
   */
    @GetMapping("/check-email/{email}")
      public boolean checkEmail(
          @PathVariable("email") String email
      ){
          return userService.checkEmail(email) ;
      }

}
