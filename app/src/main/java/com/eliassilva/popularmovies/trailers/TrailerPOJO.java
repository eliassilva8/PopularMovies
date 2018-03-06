package com.eliassilva.popularmovies.trailers;

/**
 * Created by Elias on 28/02/2018.
 */

/**
 * Object that contains information related to a single trailer
 */
public class TrailerPOJO {
    private String mTrailerKey;
    private String mTrailerName;

    public TrailerPOJO(String trailerKey, String trailerName) {
        this.mTrailerKey = trailerKey;
        this.mTrailerName = trailerName;
    }

    public String getTrailerKey() {
        return mTrailerKey;
    }

    public String getTrailerName() {
        return mTrailerName;
    }
}
