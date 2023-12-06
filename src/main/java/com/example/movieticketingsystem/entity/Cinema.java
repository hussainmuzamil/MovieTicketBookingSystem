package com.example.movieticketingsystem.entity;

import com.example.movieticketingsystem.config.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "cinema")
public class Cinema extends Auditable<Cinema> {

    @Id
    private long id;

    private String cinemaName;

    private Double lat;

    private Double lon;

    private String city;

    @OneToMany(mappedBy = "movie")
    private List<Screen> screenList;

    @OneToMany(mappedBy = "cinema")
    private List<Schedule> scheduleList;
}
