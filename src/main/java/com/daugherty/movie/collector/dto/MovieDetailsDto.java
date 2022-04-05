package com.daugherty.movie.collector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MovieDetailsDto extends RepresentationModel<MovieDto> {
    private long id;

    @NotNull
    @Schema(description = "The title of the movie")
    private String title;

    private String tagline;
    private boolean adult;

    @JsonProperty("release_date")
    private Date releaseDate;

    @JsonProperty("vote_average")
    private double voteAverage;

    private int runtime;

    private List<ReviewValue> reviews;
}