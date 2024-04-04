package com.example.userservice.Service;

import com.example.Models.User;
import com.example.Dto.UserDto;
import com.example.userservice.Exceptions.ServiceException;
import com.example.userservice.Helper.Mapper;
import com.example.userservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * This method simply add user
     * @param userDto is data transfer object for User Model
     * @return string response if User is created successfully
     */
    public User addUser(User userDto)  {
        if(
            userDto.getUser_name().trim().equals("")||
            userDto.getEmail().trim().equals("")||
            userDto.getCnic() == 0 ||
            userDto == null
        ){
            throw new ServiceException("Object Invalid");
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepo.save(userDto);
    }
    /**
     * This method returns all the users present in our records
     * @return collection of user in form of data transfer objects
     */
    public List<UserDto> getUsers() {
        List<User> allUser = userRepo.findAll();
        return allUser.stream()
            .map(user -> Mapper.UserToUserDto(user))
            .collect(Collectors.toList());
    }
    /**
     * This method update existing user with new user based upon id
     * @param targetUser is new user that will replace the old user
     * @param id will identify the previous user object
     * @return response in string if user is updated successfully
     */
    public User update(long id, User targetUser) {
        if(
            targetUser.getUser_name().trim().equals("")||
                targetUser.getEmail().trim().equals("")||
                targetUser.getCnic() == 0 ||
                targetUser == null
        ){
            throw new ServiceException("Object Invalid");
        }
        User user = userRepo.findById(id).get();
        user.setName(targetUser.getName());
        user.setUser_name(targetUser.getUser_name());
        user.setCnic(targetUser.getCnic());
        user.setEmail(targetUser.getEmail());
        user.setPassword(passwordEncoder.encode(targetUser.getPassword()));
        if(targetUser.getPath() != null){
            user.setPath(targetUser.getPath());
        }
        return userRepo.save(user);
    }

    /**
     * This method authenticate the user based upon their credentials
     * @param email is credentials for checking user authentication
     * @param pass is credentials for verifying user password
     * @return string response whether user is authenticated , not authenticated or user not exists
     */
    public String auth(String email, String pass){
        Boolean userExists = userRepo.existsByEmail(email);
        if(!userExists){
            return "no_user";

        }
        User user = userRepo.findUserByEmail(email);
        boolean choice = passwordEncoder.matches(pass,user.getPassword());
        return choice ? "true" : "wrong_password";

    }

    /**
     * This method returns UserDto based upon email
     * @param email will identify user
     * @return UserDto
     */
    public User getUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    /**
     * This will return user having specfic id
     * @param id will identify user having that id
     * @return user matched with id
     */
    public User findById(long id){
        return userRepo.findById(id).get();
    }
    /**
     * This will return whether the cnic is already assigned or not
     * @param cnic to check cnic that user want to register with
     * @return boolean response
     */
    public boolean checkCnic(String cnic) {
        return (userRepo.findByCnic(Long.valueOf(cnic)) == null);
    }
    /**
     * This will return whether the username is already assigned or not
     * @param username to check username that user want to register with
     * @return boolean response
     */
    public boolean checkUsername(String username) {
        return (userRepo.findByUser_name(username) == null);
    }
    /**
     * This will return whether the email is already assigned or not
     * @param email to check username that email want to register with
     * @return boolean response
     */
    public boolean checkEmail(String email) {
        return (userRepo.findByEmail(email) == null);
    }
}
