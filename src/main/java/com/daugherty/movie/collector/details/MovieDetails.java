package com.daugherty.movie.collector.details;

/**
 * A set of additional details provided by an external movie details provider.
 *
 * @see MovieDetailsService
 */
public interface MovieDetails {
    long getId();

    String getTitle();

    String getTagline();

    String getOverview();

    boolean isAdult();

    java.util.Date getReleaseDate();

    double getVoteAverage();

    int getRuntime();
}
