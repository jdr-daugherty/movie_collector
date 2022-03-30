package com.daugherty.movie.collector.model;

public final class ReviewUpdater {

    public static Review apply(Review source, Review target) {
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
