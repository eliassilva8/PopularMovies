package com.eliassilva.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.eliassilva.popularmovies.BuildConfig;
import com.eliassilva.popularmovies.movies.MoviePOJO;
import com.eliassilva.popularmovies.reviews.ReviewPOJO;
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

/**
 * Helper methods related to requesting and receiving movies, trailers and reviews data from themoviedb api.
 */
public class NetworkUtils {
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = BuildConfig.API_KEY;
    private final static String API_KEY_PARAM = "api_key";
    private final static String TRAILER_PARAM = "/videos";
    private final static String REVIEW_PARAM = "/reviews";

    /**
     * Build the URL to get the movies from the api
     *
     * @param sortByQuery string to sort the movies by popularity or top rated
     * @return URL to get the response
     */
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

    /**
     * Build the URL to get the trailers from a determinate movie
     * @param movieId from which we want to get the trailers
     * @return URL to get the response
     */
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

    /**
     * Build the URL to get the reviews from a determinate movie
     *
     * @param movieId from which we want to get the reviews
     * @return URL to get the response
     */
    private static URL buildReviewsUrl(String movieId) {
        try {
            Uri builtUri = Uri.parse(BASE_URL + movieId + REVIEW_PARAM).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Cannot create url: " + e);
        }
    }

    /**
     * Get the response from the api
     * @param url from which we want to get a response
     * @return a response from the api
     */
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

    /**
     * Converts the response into a JSON and extract the movies data
     * @param sortBy option choosen by the user to sort the movies
     * @return a list of movies
     */
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

    /**
     * Converts the response into a JSON and extract the trailers data
     * @param movieId from where the data will be extracted
     * @return a list of trailers
     */
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

    /**
     * Converts the response into a JSON and extract the reviews data
     *
     * @param movieId from where the data will be extracted
     * @return a list of reviews
     */
    public static List<ReviewPOJO> extractReviewsFromJson(String movieId) {
        final String REVIEWS_RESULTS = "results";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String REVIEW_URL = "url";
        List<ReviewPOJO> reviewsList = new ArrayList<>();

        try {
            URL url = buildReviewsUrl(movieId);
            String response = getResponse(url);
            JSONObject reviewsJson = new JSONObject(response);
            JSONArray reviewsResults = reviewsJson.getJSONArray(REVIEWS_RESULTS);
            for (int i = 0; i < reviewsResults.length(); i++) {
                JSONObject singleReview = reviewsResults.getJSONObject(i);
                String author = singleReview.optString(REVIEW_AUTHOR);
                String content = singleReview.optString(REVIEW_CONTENT);
                String reviewUrl = singleReview.optString(REVIEW_URL);
                reviewsList.add(new ReviewPOJO(author, content, reviewUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }
}
