package com.daugherty.movie.collector.test.factory;

import com.daugherty.movie.collector.dto.ReviewDto;
import com.daugherty.movie.collector.model.Review;

import java.util.Date;
import java.util.List;

public final class ReviewFactory {

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
}
