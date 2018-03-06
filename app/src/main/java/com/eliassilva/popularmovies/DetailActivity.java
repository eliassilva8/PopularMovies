package com.eliassilva.popularmovies;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.eliassilva.popularmovies.movies.MoviePOJO;
import com.eliassilva.popularmovies.trailers.TrailerAdapter;
import com.eliassilva.popularmovies.trailers.TrailerLoader;
import com.eliassilva.popularmovies.trailers.TrailerPOJO;
import com.eliassilva.popularmovies.utilities.NetworkReceiver;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Elias on 21/02/2018.
 */

/**
 * Shows the details of the selected movie
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<TrailerPOJO>>, TrailerAdapter.TrailerAdapterOnClickHandler, NetworkReceiver.NetworkReceiverListener {
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185/";
    private TrailerAdapter mTrailerAdapter;
    private LoaderManager mLoader;
    private static final int ID_TRAILER_LOADER = 200;
    private String mMovieId;
    private NetworkReceiver mReceiver;

    @BindView(R.id.movie_poster_detail_iv)
    ImageView mPosterPath;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        MoviePOJO movieData = (MoviePOJO) getIntent().getParcelableExtra("movie_data");

        assert movieData != null;
        Picasso.with(this).load(POSTER_BASE_URL + POSTER_SIZE + movieData.getPosterPath()).into(mPosterPath);
        mTitle.setText(movieData.getTitle());
        mReleaseDate.setText(movieData.getReleaseDate());
        mUserRating.setText(movieData.getUserRating());
        mSynopsis.setText(movieData.getSynopsis());
        mMovieId = movieData.getMovieId();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSynopsis.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        mLoader = getLoaderManager();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetworkReceiver();
        this.registerReceiver(mReceiver, filter);
        mReceiver.setNetworkReceiverListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailersList.setLayoutManager(layoutManager);
        mTrailersList.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailersList.setAdapter(mTrailerAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            this.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public Loader<List<TrailerPOJO>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_TRAILER_LOADER:
                return new TrailerLoader(this, mMovieId);
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

    @Override
    public void onConnectionChange(boolean wasTrueFlag) {
        if (wasTrueFlag) {
            mNoTrailers.setVisibility(View.GONE);
            mTrailersList.setVisibility(View.VISIBLE);
            mLoader.initLoader(ID_TRAILER_LOADER, null, DetailActivity.this);
        } else {
            mNoTrailers.setVisibility(View.VISIBLE);
            mTrailersList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(webIntent);
        }
    }
}
