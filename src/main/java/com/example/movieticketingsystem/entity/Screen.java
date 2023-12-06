package com.example.movieticketingsystem.entity;

import com.example.movieticketingsystem.config.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "screen")
@Builder
public class Screen extends Auditable<Screen> {
    @Id
    private long id;

    private String screenName;

    private int noOfSeats;

    private int screenNo;

    @ManyToOne(fetch = FetchType.EAGER)
    private Cinema cinema;
}
