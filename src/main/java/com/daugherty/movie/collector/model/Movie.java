package com.daugherty.movie.collector.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    // The ID assigned to this movie by the external movie details service.
    @Column(name = "details_id", nullable = false)
    private long detailsId;

    public Movie(String title, long detailsId) {
        this.title = title;
        this.detailsId = detailsId;
    }
}
