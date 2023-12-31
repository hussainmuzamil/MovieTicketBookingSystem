package com.example.movieticketingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket")
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private TicketClass ticketClass;

    @OneToOne
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.EAGER)
    private AppUser user;

}
