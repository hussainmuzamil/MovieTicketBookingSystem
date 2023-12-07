package com.example.movieticketingsystem.repository;

import com.example.movieticketingsystem.entity.TicketClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface TicketClassRepository extends JpaRepository<TicketClass,Long> {
}
