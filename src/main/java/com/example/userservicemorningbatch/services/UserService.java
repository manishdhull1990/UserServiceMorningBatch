package com.example.userservicemorningbatch.services;

import com.example.userservicemorningbatch.exception.InvalidPasswordException;
import com.example.userservicemorningbatch.exception.InvalidTokenException;
import com.example.userservicemorningbatch.models.Token;
import com.example.userservicemorningbatch.models.User;
import com.example.userservicemorningbatch.repositories.TokenRepository;
import com.example.userservicemorningbatch.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private TokenRepository tokenRepository;
    UserService(UserRepository userRepository,
                BCryptPasswordEncoder bCryptPasswordEncoder,
                TokenRepository tokenRepository){
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.tokenRepository=tokenRepository;
    }
    public User signUp(String email,String password,String name){
        Optional<User> optionalUser=userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            //user is already present in the DB, so no need to sign up
            return optionalUser.get();
        }
        else {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setHashedPassword(bCryptPasswordEncoder.encode(password));
            User savedUser = userRepository.save(user);
            return savedUser;
        }
    }

    public Token login(String email, String password) throws InvalidPasswordException{
        /*
        1. Check if the user exists with the given email or not.
        2. If not, throw an exception and redirect user to signup.
        3. If yes, compare the incoming password with the password sotred in database.
        4. If password matched then login successful and return new token.
         */
        Optional<User> verifiedUser=userRepository.findByEmail(email);
        if (verifiedUser.isEmpty()){
            //User is not present in the database
            return null;
        }
        User user=verifiedUser.get();
        if (!bCryptPasswordEncoder.matches(password,user.getHashedPassword())) {
            // Throw an exception
            throw new InvalidPasswordException("Please enter correct password");
        }
        // Login successful, generate a new token.
        Token token=generateToken(user);
        Token savedToken = tokenRepository.save(token);
        return savedToken;
    }

    private Token generateToken(User user){
        LocalDate currentTime = LocalDate.now(); //current time
        LocalDate thirtyDaysFromCurrentTime = currentTime.plusDays(30);
        Date expiryDate = Date.from(thirtyDaysFromCurrentTime.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token=new Token();
        token.setExpiryAt(expiryDate);

        // Token is a randomly generated string of 128 characters
        token.setTokenValue(RandomStringUtils.randomAlphanumeric(120));
        token.setUser(user);

        return token;
    }

    public void logout(String tokenValue) throws InvalidTokenException {
        //Validate is the given token is present in the DB as well as is_deleted = false
        Optional<Token> optionalToken = tokenRepository.findByTokenValueAndDeleted(
                tokenValue,
                false
        );

        if (optionalToken.isEmpty()){
            //Throw an exception
            throw new InvalidTokenException("Invalid token passed");
        }
        Token token =optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
        return;
    }

    public User validateToken(String token) throws InvalidTokenException {
        Optional<Token> optionalToken = tokenRepository.findByTokenValueAndDeleted(token,false);

        if (optionalToken.isEmpty()){
            throw new InvalidTokenException("Invalid token passed");
        }
        return optionalToken.get().getUser();
    }
}
