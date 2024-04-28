package com.example.userservicemorningbatch.controllers;

import com.example.userservicemorningbatch.dtos.*;
import com.example.userservicemorningbatch.exception.InvalidPasswordException;
import com.example.userservicemorningbatch.exception.InvalidTokenException;
import com.example.userservicemorningbatch.models.Token;
import com.example.userservicemorningbatch.models.User;
import com.example.userservicemorningbatch.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) throws InvalidPasswordException {
         Token token=userService.login(
                 loginRequestDto.getEmail(),
                 loginRequestDto.getPassword());
         LoginResponseDto loginResponseDto=new LoginResponseDto();
         loginResponseDto.setToken(token);
        return loginResponseDto;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogOutRequestDto logOutRequestDto) throws InvalidTokenException
    {
        ResponseEntity<Void> responseEntity=null;
        try{
            userService.logout(logOutRequestDto.getToken());
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println("Something went wrong");
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable  String token) throws InvalidTokenException {
            return UserDto.from(userService.validateToken(token));
    }
}
