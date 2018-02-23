package com.eliassilva.popularmoviesstage1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.eliassilva.popularmoviesstage1.customExceptions.FetchDataException;
import com.eliassilva.popularmoviesstage1.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by Elias on 21/02/2018.
 */

class MovieLoader extends AsyncTaskLoader<List<MoviePOJO>> {
    private final String mSortBy;

    public MovieLoader(Context context, String sortBy) {
        super(context);
        mSortBy = sortBy;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MoviePOJO> loadInBackground() {
        if (mSortBy == null) {
            return null;
        }
        List<MoviePOJO> moviesList = null;
        try {
            moviesList = NetworkUtils.fetchMoviesData(mSortBy);

        } catch (FetchDataException e) {
            e.printStackTrace();
        }
        return moviesList;
    }
}
