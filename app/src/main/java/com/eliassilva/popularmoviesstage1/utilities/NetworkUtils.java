package com.eliassilva.popularmoviesstage1.utilities;

import android.net.Uri;
import android.util.Log;

import com.eliassilva.popularmoviesstage1.BuildConfig;
import com.eliassilva.popularmoviesstage1.MoviePOJO;
import com.eliassilva.popularmoviesstage1.customExceptions.FetchDataException;

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

    private static URL buildUrl(String sortByQuery) throws MalformedURLException {
        Uri builtUri = Uri.parse(BASE_URL + sortByQuery).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        Log.d("*+*+URL = ", builtUri.toString());
        return new URL(builtUri.toString());
    }

    private static String getResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static List<MoviePOJO> extractMoviesFromJson(String response) throws JSONException {
        final String MOVIES_RESULTS = "results";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_TITLE = "title";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_USER_RATING = "vote_average";
        final String MOVIE_SYNOPSIS = "overview";

        JSONObject moviesJson = new JSONObject(response);
        JSONArray moviesResultsArray = moviesJson.getJSONArray(MOVIES_RESULTS);
        List<MoviePOJO> moviesList = new ArrayList<>();
        for (int i = 0; i < moviesResultsArray.length(); i++) {
            JSONObject singleMovieResult = moviesResultsArray.getJSONObject(i);
            String posterPath = singleMovieResult.optString(MOVIE_POSTER_PATH);
            String title = singleMovieResult.optString(MOVIE_TITLE);
            String releaseDate = singleMovieResult.optString(MOVIE_RELEASE_DATE);
            String userRating = Double.toString(singleMovieResult.optDouble(MOVIE_USER_RATING));
            String synopsis = singleMovieResult.getString(MOVIE_SYNOPSIS);
            moviesList.add(new MoviePOJO(posterPath, title, releaseDate, userRating, synopsis));
        }
        return moviesList;
    }

    public static List<MoviePOJO> fetchMoviesData(String sortByQuery) throws FetchDataException {
        URL url;
        String response;
        List<MoviePOJO> moviesList;
        try {
            url = buildUrl(sortByQuery);
            response = getResponse(url);
            moviesList = extractMoviesFromJson(response);
        } catch (MalformedURLException e) {
            throw new FetchDataException("Cannot create url: " + e);
        } catch (IOException e) {
            throw new FetchDataException("Error making HTTP request: " + e);
        } catch (JSONException e) {
            throw new FetchDataException("Cannot extract data from JSON: " + e);
        }
        return moviesList;
    }
}
