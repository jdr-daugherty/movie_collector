package com.daugherty.movie.collector.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MovieDto {

    private long id;

    @NotNull
    @Schema(description = "The title of the movie")
    private String title;

    @Schema(description = "The ID assigned to this movie by the external movie details service.")
    private long detailsId;
}
