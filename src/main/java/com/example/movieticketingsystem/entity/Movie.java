package com.example.movieticketingsystem.entity;

import com.example.movieticketingsystem.config.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "movie")
public class Movie extends Auditable<Movie> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private int rating;

    private long duration;

    private String genre;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    private List<Schedule> scheduleList;

}
