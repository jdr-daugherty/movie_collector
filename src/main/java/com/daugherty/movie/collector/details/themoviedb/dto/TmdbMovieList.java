package com.daugherty.movie.collector.details.themoviedb.dto;

import lombok.Data;

import java.util.List;

@Data
public class TmdbMovieList {
    private List<TmdbMovie> results;
}
