package com.eliassilva.popularmovies.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.Uri;

import com.eliassilva.popularmovies.movies.MoviePOJO;
import com.eliassilva.popularmovies.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by Elias on 15/03/2018.
 */

public class FavoriteLoader extends android.support.v4.content.AsyncTaskLoader<List<MoviePOJO>> {
    private final Uri mUri;

    public FavoriteLoader(Context context, Uri uri) {
        super(context);
        mUri = uri;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MoviePOJO> loadInBackground() {
        if (mUri == null) {
            return null;
        }
        return NetworkUtils.getMoviesFromDatabase(getContext(), mUri);
    }
}
