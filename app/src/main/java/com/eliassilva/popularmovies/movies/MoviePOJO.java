package com.eliassilva.popularmovies.movies;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elias on 17/02/2018.
 */

/**
 * Object that contains information related to a single movie
 */
public class MoviePOJO implements Parcelable {
    private int mMovieId;
    private String mPosterPath;
    private String mTitle;
    private String mReleaseDate;
    private String mUserRating;
    private String mSynopsis;
    private Boolean mIsFavorite;

    public MoviePOJO(int movieId, String poster, String title, String releaseDate, String userRating, String synopsis, Boolean isFavorite) {
        mMovieId = movieId;
        mPosterPath = poster;
        mTitle = title;
        mSynopsis = synopsis;
        mReleaseDate = releaseDate;
        mUserRating = userRating;
        mIsFavorite = isFavorite;
    }

    protected MoviePOJO(Parcel in) {
        mMovieId = in.readInt();
        mPosterPath = in.readString();
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mUserRating = in.readString();
        mSynopsis = in.readString();
        mIsFavorite = in.readByte() != 0;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public Boolean getIsFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(Boolean mIsFavorite) {
        this.mIsFavorite = mIsFavorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mPosterPath);
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mUserRating);
        dest.writeString(mSynopsis);
        dest.writeByte((byte) (mIsFavorite ? 1 : 0));
    }

    public static final Parcelable.Creator<MoviePOJO> CREATOR = new Parcelable.Creator<MoviePOJO>() {
        @Override
        public MoviePOJO createFromParcel(Parcel source) {
            return new MoviePOJO(source);
        }

        @Override
        public MoviePOJO[] newArray(int size) {
            return new MoviePOJO[size];
        }
    };
}
