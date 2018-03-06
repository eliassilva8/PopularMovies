package com.eliassilva.popularmovies.reviews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.eliassilva.popularmovies.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by Elias on 06/03/2018.
 */

/**
 * Perform async load of review data
 */
public class ReviewLoader extends AsyncTaskLoader<List<ReviewPOJO>> {
    private final String mMovieId;

    public ReviewLoader(Context context, String movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<ReviewPOJO> loadInBackground() {
        if (mMovieId == null) {
            throw new RuntimeException("Movie ID equal to null");
        }
        return NetworkUtils.extractReviewsFromJson(mMovieId);
    }
}
