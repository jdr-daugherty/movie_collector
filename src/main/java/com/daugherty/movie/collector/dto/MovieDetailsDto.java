package com.daugherty.movie.collector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@Schema(name="movie_details")
public class MovieDetailsDto {
    @JsonProperty("id")
    private long id;

    @NotNull
    @JsonProperty("title")
    @Schema(description = "The title of the movie")
    private String title;

    @JsonProperty("tagline")
    private String tagline;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("adult")
    private boolean adult;

    @JsonProperty("release_date")
    private Date releaseDate;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("runtime")
    private int runtime;

    @JsonProperty("reviews")
    @EqualsAndHashCode.Exclude
    private List<ReviewValue> reviews;
}
