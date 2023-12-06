package com.example.movieticketingsystem.entity;

import com.example.movieticketingsystem.config.Auditable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
public class Schedule extends Auditable<Schedule> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime showTime;

    private LocalDateTime showDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Movie movie;

    @ManyToOne(fetch = FetchType.EAGER)
    private Cinema cinema;
}
