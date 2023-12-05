package com.example.movieticketingsystem.repository;

import com.example.movieticketingsystem.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser,Long> {

    Optional<AppUser> findAppUserByEmail(String email);
}
