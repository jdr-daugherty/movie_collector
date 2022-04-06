package com.daugherty.movie.collector.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewValue {

    private long id;

    @NotNull
    @Schema(description = "The user-submitted title of this review.")
    private String title;

    @Schema(description = "The optional body of a full review.")
    private String body;

    @NotNull
    @Schema(description = "The date and time that this review was last updated.")
    private Date reviewed;

    @Max(10)
    @Min(0)
    @Schema(description = "The rating associated with this review.")
    private Integer rating;
}
