package com.daugherty.movie.collector.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Relation(itemRelation = "movie", collectionRelation = "movies")
public class MovieDto extends RepresentationModel<MovieDto> {

    private long id;

    @NotNull
    @Schema(description = "The title of the movie")
    private String title;

    @Schema(description = "The ID of this movie at themoviedb.org.")
    private long tmdbId;
}
