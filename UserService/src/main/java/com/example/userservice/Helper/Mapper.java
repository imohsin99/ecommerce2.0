package com.example.userservice.Helper;

import com.example.Models.User;
import com.example.Dto.UserDto;

public class Mapper {
  public static UserDto UserToUserDto(User user){
    UserDto userDto = new UserDto();
      userDto.setUser_id(user.getUser_id());
      userDto.setName(user.getName());
      userDto.setUser_name(user.getUser_name());
      userDto.setCnic(user.getCnic());
      userDto.setEmail(user.getEmail());
      userDto.setPassword(user.getPassword());
      userDto.setPath(user.getPath());

    return userDto;
  }
  public static User UserDtoToUser(UserDto userDto){
    User user = new User();
    user.setUser_id(userDto.getUser_id());
    user.setName(userDto.getName());
    user.setUser_name(userDto.getUser_name());
    user.setCnic(userDto.getCnic());
    user.setEmail(userDto.getEmail());
    user.setPassword(userDto.getPassword());
    user.setPath(userDto.getPath());
    return user;
  }
}
