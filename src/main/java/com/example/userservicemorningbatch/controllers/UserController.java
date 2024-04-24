package com.example.userservicemorningbatch.controllers;

import com.example.userservicemorningbatch.dtos.LogOutRequestDto;
import com.example.userservicemorningbatch.dtos.LoginRequestDto;
import com.example.userservicemorningbatch.dtos.SignUpRequestDto;
import com.example.userservicemorningbatch.dtos.UserDto;
import com.example.userservicemorningbatch.models.Token;
import com.example.userservicemorningbatch.models.User;
import com.example.userservicemorningbatch.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    UserController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/signup") // localhost:8080/users/signup
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        User user=userService.signUp(
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword(),
                signUpRequestDto.getName());

        // get UserDto from user
        return UserDto.from(user);
    }

    @PostMapping("/login") // localhost:8080/users/login
    public Token login(@RequestBody LoginRequestDto loginRequestDto){
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogOutRequestDto logOutRequestDto){
        return null;
    }
}
