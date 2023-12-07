package com.example.movieticketingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "ticket_class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int price;

    private SeatType seatType;

    @ManyToMany(mappedBy = "ticketClassSet")
    private Set<Cinema> cinemaSet;
}
