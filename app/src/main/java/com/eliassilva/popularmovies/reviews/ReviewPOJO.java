package com.eliassilva.popularmovies.reviews;

/**
 * Created by Elias on 06/03/2018.
 */

/**
 * Object that contains information related to a single review
 */
public class ReviewPOJO {
    private String mReviewAuthor;
    private String mReviewContent;
    private String mReviewUrl;

    public ReviewPOJO(String reviewAuthor, String reviewContent, String reviewUrl) {
        this.mReviewAuthor = reviewAuthor;
        this.mReviewContent = reviewContent;
        this.mReviewUrl = reviewUrl;
    }

    public String getReviewAuthor() {
        return mReviewAuthor;
    }

    public String getReviewContent() {
        return mReviewContent;
    }

    public String getReviewUrl() {
        return mReviewUrl;
    }
}
