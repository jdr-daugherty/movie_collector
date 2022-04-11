package com.daugherty.movie.collector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ReviewDto {
    @JsonProperty("id")
    private long id;

    @NotNull
    @Schema(description = "The user-submitted title of this review.")
    @JsonProperty("title")
    private String title;

    @Schema(description = "The optional body of a full review.")
    @JsonProperty("body")
    private String body;

    @Schema(description = "The id of the movie reviewed.")
    @JsonProperty("movie_id")
    private long movieId;

    @NotNull
    @Schema(description = "The date and time that this review was last updated.")
    @JsonProperty("reviewed")
    private Date reviewed;

    @Max(10)
    @Min(0)
    @Schema(description = "The rating associated with this review.")
    @JsonProperty("rating")
    private Integer rating;
}
