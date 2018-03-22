package com.eliassilva.popularmovies.data;

/**
 * Created by Elias on 07/03/2018.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the favorite movies database.
 */
public class FavoritesContract {
    public static final String CONTENT_AUTHORITY = "com.eliassilva.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String TABLE_NAME = PATH_FAVORITES;
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_RELEASE_DATE = "release";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        /**
         * Builds a URI that adds the movie id to the end of the favorites content URI path.
         *
         * @param movieId
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUri(int movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(movieId))
                    .build();
        }
    }


}
