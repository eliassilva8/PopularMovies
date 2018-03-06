package com.eliassilva.popularmovies.trailers;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.eliassilva.popularmovies.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by Elias on 28/02/2018.
 */

/**
 * Perform async load of trailer data
 */
public class TrailerLoader extends AsyncTaskLoader<List<TrailerPOJO>> {
    private final String mMovieId;

    public TrailerLoader(Context context, String movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<TrailerPOJO> loadInBackground() {
        if (mMovieId == null) {
            throw new RuntimeException("Movie ID equal to null");
        }
        return NetworkUtils.extractTrailersFromJson(mMovieId);
    }
}
