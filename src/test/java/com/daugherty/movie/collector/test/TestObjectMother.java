package com.daugherty.movie.collector.test;

import com.daugherty.movie.collector.dto.MovieDetailsDto;
import com.daugherty.movie.collector.dto.MovieDto;
import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.dto.ReviewValue;
import com.daugherty.movie.collector.model.Movie;
import com.daugherty.movie.collector.model.Review;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.themoviedb.api.dto.TmdbMovie;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public final class TestObjectMother {

    public static Review reviewWithId() {
        Review expected = reviewWithoutId();
        expected.setId(2345);
        return expected;
    }

    public static Review reviewWithoutId() {
        Review review = new Review("This movie is great!", 1234);
        review.setBody("I can't believe how great this movie is and neither will you!");
        return review;
    }

    public static ReviewDto reviewDtoWithId() {
        ReviewDto dto = reviewDtoWithoutId();
        dto.setId(2345);
        return dto;
    }

    public static ReviewDto reviewDtoWithoutId() {
        ReviewDto dto = new ReviewDto();
        dto.setReviewed(new Date());
        dto.setTitle("This movie is great!");
        dto.setBody("I can't believe how great this movie is and neither will you!");
        dto.setMovieId(1234);
        return dto;
    }

    public static List<ReviewDto> twoReviewsWithIds() {
        ReviewDto first = reviewDtoWithId();

        ReviewDto second = new ReviewDto();
        second.setId(first.getId() + 1);
        second.setMovieId(first.getMovieId());
        second.setReviewed(new Date());
        second.setTitle("New Updated Title!!!");
        second.setBody("New Updated Body!");

        return List.of(first, second);
    }

    public static Movie movieWithId() {
        Movie expected = movieWithoutId();
        expected.setId(1234);
        return expected;
    }

    public static Movie movieWithoutId() {
        return new Movie("Reservoir Dogs", 500);
    }

    public static MovieDto movieDtoWithoutId() {
        MovieDto dto = new MovieDto();
        dto.setTitle("Reservoir Dogs");
        dto.setTmdbId(500);
        return dto;
    }

    public static MovieDto movieDtoWithId() {
        MovieDto dto = movieDtoWithoutId();
        dto.setId(1234);
        return dto;
    }

    public static List<MovieDto> twoMovieDtosWithIds() {
        MovieDto first = movieDtoWithId();

        MovieDto second = new MovieDto();
        second.setId(first.getId()+1);
        second.setTitle("Fight Club");
        second.setTmdbId(550);

        return List.of(first, second);
    }

    public static TmdbMovie tmdbMovie() {
        TmdbMovie m = new TmdbMovie();
        m.setReleaseDate(new Date());
        m.setTitle("Reservoir Dogs");
        m.setId(500);
        m.setTagline("Every dog has his day.");
        m.setAdult(true);
        m.setRuntime(99);
        return m;
    }

    public static MovieDetailsDto movieDetailsDto() {
        MovieDetailsDto dto = new MovieDetailsDto();

        dto.setId(1234);
        dto.setTitle("Reservoir Dogs");
        dto.setTagline("Every dog has his day.");
        dto.setAdult(true);
        dto.setRuntime(99);
        dto.setReleaseDate(new Date());
        dto.setVoteAverage(8.2);

        ReviewValue r1 = new ReviewValue();
        r1.setId(223);
        r1.setTitle("Best Dog Movie");
        r1.setBody("This dog movie was great!");
        r1.setRating(10);
        r1.setReviewed(new Date(697421284));//Fri Feb 07 1992 00:08:04 GMT+0000

         ReviewValue r2 = new ReviewValue();
        r2.setId(225);
        r2.setTitle("Love This Dog Movie");
        r2.setBody("This is the best dog movie!");
        r2.setRating(10);
        r2.setReviewed(new Date(697507684));//Sat Feb 08 1992 00:08:04 GMT+0000

        dto.setReviews(List.of(r1, r2));

        return dto;
    }
}
