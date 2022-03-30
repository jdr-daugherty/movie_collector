package org.themoviedb.api.dta;

import lombok.Data;

import java.util.List;

@Data
public class TmdbMovieList {
    private List<TmdbMovie> results;
}
