package com.example.userservicemorningbatch.repositories;

import com.example.userservicemorningbatch.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    @Override
    Token save(Token token);

    //Select * from tokens where tokenvalue=<> and is_deleted =false;
    Optional<Token> findByTokenValueAndDeleted(String tokenValue,boolean isDeleted);
}
