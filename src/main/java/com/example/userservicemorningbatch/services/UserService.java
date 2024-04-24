package com.example.userservicemorningbatch.services;

import com.example.userservicemorningbatch.models.Token;
import com.example.userservicemorningbatch.models.User;
import com.example.userservicemorningbatch.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    UserService(UserRepository userRepository,
                BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }
    public User signUp(String email,String password,String name){
        Optional<User> optionalUser=userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            //user is already present in the DB, so no need to sign up
            return optionalUser.get();
        }

        User user=new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        User savedUser= userRepository.save(user);
        return savedUser;
    }

    public Token login(String email, String password){
        return null;
    }

    public void logout(Token token,String password,String name){
        return;
    }
}
