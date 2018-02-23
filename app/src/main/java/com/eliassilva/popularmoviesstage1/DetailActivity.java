package com.eliassilva.popularmoviesstage1;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Elias on 21/02/2018.
 */

public class DetailActivity extends AppCompatActivity {
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185/";

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSynopsis.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}
