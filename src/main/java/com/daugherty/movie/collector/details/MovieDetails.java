package com.daugherty.movie.collector.details;

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
