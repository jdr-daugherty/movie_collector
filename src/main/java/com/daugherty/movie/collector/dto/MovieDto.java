package com.daugherty.movie.collector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="movie")
public class MovieDto {
    @JsonProperty("id")
    private long id;

    @Schema(description = "The title of the movie")
    @JsonProperty("title")
    private String title;

    @Schema(description = "The ID assigned to this movie by the external movie details service.")
    @JsonProperty("details_id")
    private long detailsId;
}
