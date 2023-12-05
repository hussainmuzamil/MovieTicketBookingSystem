package com.example.movieticketingsystem.repository;

import com.example.movieticketingsystem.entity.AppUser;
import com.example.movieticketingsystem.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findByToken(String token);

    @Query("select t from Token t join AppUser u on t.appUser = u where u =: user and (t.expired = false or t.revoked =false) ")
    List<Token> findAllValidTokenByUser(AppUser user);
}
