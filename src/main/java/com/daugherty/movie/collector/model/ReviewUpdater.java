package com.daugherty.movie.collector.model;

import com.daugherty.movie.collector.dto.ReviewDto;

public final class ReviewUpdater {

    public static Review apply(ReviewDto source, Review target) {
        if (source.getTitle() != null) {
            target.setTitle(source.getTitle());
        }
        if (source.getBody() != null) {
            target.setBody(source.getBody());
        }
        if (source.getReviewed() != null && source.getReviewed().after(target.getReviewed())) {
            target.setReviewed(source.getReviewed());
        }
        if (source.getRating() != null) {
            target.setRating(source.getRating());
        }
        return target;
    }
}
