package com.example.thymeleafapp.Model;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long user_id;

    private String name;
    private String user_name;
    private long cnic;
    private String email;
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", cnic=" + cnic +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    private String path;



}
