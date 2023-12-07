package com.example.movieticketingsystem.entity;

import com.example.movieticketingsystem.config.Auditable;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "cinema")
public class Cinema extends Auditable<Cinema> {

    @Id
    private long id;

    private String cinemaName;

    private Double lat;

    private Double lon;

    private String city;

    @OneToMany(mappedBy = "cinema")
    private List<Screen> screenList;

    @OneToMany(mappedBy = "cinema")
    private List<Schedule> scheduleList;

    @ManyToMany
    @JoinTable(name = "cinema_ticketClass",
            joinColumns = @JoinColumn(name = "cinema_id")
            ,inverseJoinColumns = @JoinColumn(name = "ticket_class_id"))
    private Set<TicketClass> ticketClassSet = new HashSet<>();
}
