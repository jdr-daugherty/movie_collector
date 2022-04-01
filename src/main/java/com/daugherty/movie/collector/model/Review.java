package com.daugherty.movie.collector.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Review  extends RepresentationModel<Review> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Schema(description = "The user-submitted title of this review.")
    private String title;

    @Schema(description = "The optional body of a full review.")
    private String body;

    @Schema(description = "The id of the movie reviewed.")
    private long movieId;

    @NotNull
    @Schema(description = "The date and time that this review was last updated.")
    private Date reviewed;

    @Max(10)
    @Min(0)
    @Schema(description = "The rating associated with this review.")
    private Integer rating;

    public Review(String title, long movieId) {
        this.title = title;
        this.movieId = movieId;
        this.reviewed = new Date();
        this.rating = 5;
    }
}
