package com.eliassilva.popularmovies.reviews;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.eliassilva.popularmovies.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by Elias on 06/03/2018.
 */

/**
 * Perform async load of review data
 */
public class ReviewLoader extends AsyncTaskLoader<List<ReviewPOJO>> {
    private final int mMovieId;

    public ReviewLoader(Context context, int movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<ReviewPOJO> loadInBackground() {
        if (mMovieId == 0) {
            throw new RuntimeException("Movie ID equal to null");
        }
        return NetworkUtils.extractReviewsFromJson(mMovieId);
    }
}
