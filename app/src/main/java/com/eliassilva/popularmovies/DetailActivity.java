package com.eliassilva.popularmovies;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.eliassilva.popularmovies.data.FavoritesContract.FavoriteEntry;
import com.eliassilva.popularmovies.movies.MoviePOJO;
import com.eliassilva.popularmovies.reviews.ReviewAdapter;
import com.eliassilva.popularmovies.reviews.ReviewLoader;
import com.eliassilva.popularmovies.reviews.ReviewPOJO;
import com.eliassilva.popularmovies.trailers.TrailerAdapter;
import com.eliassilva.popularmovies.trailers.TrailerLoader;
import com.eliassilva.popularmovies.trailers.TrailerPOJO;
import com.eliassilva.popularmovies.utilities.NetworkReceiver;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Elias on 21/02/2018.
 */

/**
 * Shows the details of the selected movie
 */
public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler, NetworkReceiver.NetworkReceiverListener {
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185/";
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private LoaderManager mLoader;
    private static final int ID_TRAILER_LOADER = 200;
    private static final int ID_REVIEW_LOADER = 300;
    private int mMovieId;
    private NetworkReceiver mReceiver;
    private static final String MOVIE_ID = "movie_id";
    Bundle mBundle = new Bundle();

    private LoaderManager.LoaderCallbacks<List<TrailerPOJO>> mTrailerLoader = new LoaderManager.LoaderCallbacks<List<TrailerPOJO>>() {
        @Override
        public Loader<List<TrailerPOJO>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case ID_TRAILER_LOADER:
                    return new TrailerLoader(DetailActivity.this, args.getInt(MOVIE_ID));
                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
            }
        }

        @Override
        public void onLoadFinished(Loader<List<TrailerPOJO>> loader, List<TrailerPOJO> data) {
            mTrailerAdapter.setTrailerData(data);
        }

        @Override
        public void onLoaderReset(Loader<List<TrailerPOJO>> loader) {
            mTrailerAdapter.setTrailerData(null);
        }
    };

    private LoaderManager.LoaderCallbacks<List<ReviewPOJO>> mReviewLoader = new LoaderManager.LoaderCallbacks<List<ReviewPOJO>>() {
        @Override
        public Loader<List<ReviewPOJO>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case ID_REVIEW_LOADER:
                    return new ReviewLoader(DetailActivity.this, args.getInt(MOVIE_ID));
                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
            }
        }

        @Override
        public void onLoadFinished(Loader<List<ReviewPOJO>> loader, List<ReviewPOJO> data) {
            mReviewAdapter.setReviewData(data);
        }

        @Override
        public void onLoaderReset(Loader<List<ReviewPOJO>> loader) {
            mReviewAdapter.setReviewData(null);
        }
    };

    @BindView(R.id.movie_poster_detail_iv)
    ImageView mPoster;
    @BindView(R.id.movie_title_tv)
    TextView mTitle;
    @BindView(R.id.movie_release_date_tv)
    TextView mReleaseDate;
    @BindView(R.id.movie_user_rating_tv)
    TextView mUserRating;
    @BindView(R.id.movie_synopsis_tv)
    TextView mSynopsis;
    @BindView(R.id.movies_trailers_rv)
    RecyclerView mTrailersList;
    @BindView(R.id.empty_trailers_tv)
    TextView mNoTrailers;
    @BindView(R.id.movies_reviews_rv)
    RecyclerView mReviewsList;
    @BindView(R.id.empty_reviews_tv)
    TextView mNoReviews;
    @BindView(R.id.favoriteButton)
    ToggleButton mFavoriteButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        final MoviePOJO movieData = (MoviePOJO) getIntent().getParcelableExtra("movie_data");
        assert movieData != null;
        mTitle.setText(movieData.getTitle());
        mReleaseDate.setText(movieData.getReleaseDate());
        mUserRating.setText(movieData.getUserRating());
        mSynopsis.setText(movieData.getSynopsis());
        mMovieId = movieData.getMovieId();
        if (movieData.getIsFavorite()) {
            mFavoriteButton.setChecked(true);
            Picasso.with(this).load(new File(this.getFilesDir().getAbsolutePath() + movieData.getPosterPath())).into(mPoster);
        } else {
            Picasso.with(this).load(POSTER_BASE_URL + POSTER_SIZE + movieData.getPosterPath()).into(mPoster);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSynopsis.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        mLoader = getLoaderManager();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetworkReceiver();
        this.registerReceiver(mReceiver, filter);
        mReceiver.setNetworkReceiverListener(this);

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        mTrailersList.setLayoutManager(trailerLayoutManager);
        mTrailersList.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailersList.setAdapter(mTrailerAdapter);

        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        mReviewsList.setLayoutManager(reviewLayoutManager);
        mReviewsList.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewsList.setAdapter(mReviewAdapter);

        mBundle.putInt(MOVIE_ID, mMovieId);

        mFavoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentResolver contentResolver = getContentResolver();
                if (isChecked) {
                    ContentValues values = new ContentValues();
                    values.put(FavoriteEntry.COLUMN_ID, movieData.getMovieId());
                    values.put(FavoriteEntry.COLUMN_NAME, movieData.getTitle());
                    values.put(FavoriteEntry.COLUMN_RELEASE_DATE, movieData.getReleaseDate());
                    values.put(FavoriteEntry.COLUMN_RATING, movieData.getUserRating());
                    values.put(FavoriteEntry.COLUMN_SYNOPSIS, movieData.getSynopsis());
                    values.put(FavoriteEntry.COLUMN_POSTER_PATH, saveImage(movieData.getPosterPath()));
                    contentResolver.insert(FavoriteEntry.CONTENT_URI, values);
                    Toast.makeText(DetailActivity.this, "Movie inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Uri uri = FavoriteEntry.buildMovieUri(movieData.getMovieId());
                    contentResolver.delete(uri, null, null);
                    DetailActivity.this.deleteFile(movieData.getPosterPath().replace("/", ""));
                    Toast.makeText(DetailActivity.this, "Movie removed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            this.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onConnectionChange(boolean wasTrueFlag) {
        if (wasTrueFlag) {
            mNoTrailers.setVisibility(View.GONE);
            mTrailersList.setVisibility(View.VISIBLE);
            mNoReviews.setVisibility(View.GONE);
            mReviewsList.setVisibility(View.VISIBLE);
            mLoader.initLoader(ID_TRAILER_LOADER, mBundle, mTrailerLoader);
            mLoader.initLoader(ID_REVIEW_LOADER, mBundle, mReviewLoader);
        } else {
            mNoTrailers.setVisibility(View.VISIBLE);
            mTrailersList.setVisibility(View.GONE);
            mNoReviews.setVisibility(View.VISIBLE);
            mReviewsList.setVisibility(View.GONE);
        }
    }

    @Override
    public void playTrailer(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(webIntent);
        }
    }

    @Override
    public void seeReview(String reviewUrl) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewUrl));
        startActivity(webIntent);
    }

    /**
     * Save an image into the app directory
     *
     * @param imageName
     */
    private String saveImage(String imageName) {
        mPoster.setDrawingCacheEnabled(true);
        mPoster.buildDrawingCache(true);
        imageName = imageName.replace("/", "");
        try {
            Bitmap image = Bitmap.createBitmap(mPoster.getDrawingCache());
            FileOutputStream fileOutputStream = openFileOutput(imageName, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 0, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(imageName);
        return file.getAbsolutePath();
    }
}
