package com.eliassilva.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.eliassilva.popularmovies.BuildConfig;
import com.eliassilva.popularmovies.movies.MoviePOJO;
import com.eliassilva.popularmovies.trailers.TrailerPOJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Elias on 18/02/2018.
 */

public class NetworkUtils {
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = BuildConfig.API_KEY;
    private final static String API_KEY_PARAM = "api_key";
    private final static String TRAILER_PARAM = "/videos";

    private static URL buildMoviesUrl(String sortByQuery) {
        try {
            Uri builtUri = Uri.parse(BASE_URL + sortByQuery).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Cannot create url: " + e);
        }
    }

    private static URL buildTrailersUrl(String movieId) {
        try {
            Uri builtUri = Uri.parse(BASE_URL + movieId + TRAILER_PARAM).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Cannot create url: " + e);
        }

    }

    private static String getResponse(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error making HTTP request: " + e);
        } finally {
            urlConnection.disconnect();
        }
    }

    public static List<MoviePOJO> extractMoviesFromJson(String sortBy) {
        final String MOVIES_RESULTS = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_TITLE = "title";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_USER_RATING = "vote_average";
        final String MOVIE_SYNOPSIS = "overview";
        List<MoviePOJO> moviesList = new ArrayList<>();

        try {
            URL url = buildMoviesUrl(sortBy);
            String response = getResponse(url);
            JSONObject moviesJson = new JSONObject(response);
            JSONArray moviesResultsArray = moviesJson.getJSONArray(MOVIES_RESULTS);
            for (int i = 0; i < moviesResultsArray.length(); i++) {
                JSONObject singleMovieResult = moviesResultsArray.getJSONObject(i);
                String movieId = singleMovieResult.getString(MOVIE_ID);
                String posterPath = singleMovieResult.optString(MOVIE_POSTER_PATH);
                String title = singleMovieResult.optString(MOVIE_TITLE);
                String releaseDate = singleMovieResult.optString(MOVIE_RELEASE_DATE);
                String userRating = Double.toString(singleMovieResult.optDouble(MOVIE_USER_RATING));
                String synopsis = singleMovieResult.getString(MOVIE_SYNOPSIS);
                moviesList.add(new MoviePOJO(movieId, posterPath, title, releaseDate, userRating, synopsis));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesList;
    }

    public static List<TrailerPOJO> extractTrailersFromJson(String movieId) {
        final String TRAILER_RESULTS = "results";
        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";
        List<TrailerPOJO> trailersList = new ArrayList<>();

        try {
            URL url = buildTrailersUrl(movieId);
            String response = getResponse(url);
            JSONObject trailerJson = new JSONObject(response);
            JSONArray trailerResults = trailerJson.getJSONArray(TRAILER_RESULTS);
            for (int i = 0; i < trailerResults.length(); i++) {
                JSONObject singleTrailer = trailerResults.getJSONObject(i);
                String key = singleTrailer.optString(TRAILER_KEY);
                String name = singleTrailer.optString(TRAILER_NAME);
                trailersList.add(new TrailerPOJO(key, name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailersList;
    }
}
