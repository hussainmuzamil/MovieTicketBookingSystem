package com.example.movieticketingsystem.repository;

import com.example.movieticketingsystem.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema,Long> {

}
