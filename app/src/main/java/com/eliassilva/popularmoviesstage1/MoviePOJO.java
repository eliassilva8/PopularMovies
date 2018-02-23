package com.eliassilva.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elias on 17/02/2018.
 */

public class MoviePOJO implements Parcelable {
    private String mPosterPath;
    private String mTitle;
    private String mReleaseDate;
    private String mUserRating;
    private String mSynopsis;

    public MoviePOJO(String poster, String title, String releaseDate, String userRating, String synopsis) {
        mPosterPath = poster;
        mTitle = title;
        mSynopsis = synopsis;
        mReleaseDate = releaseDate;
        mUserRating = userRating;
    }

    protected MoviePOJO(Parcel in) {
        mPosterPath = in.readString();
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mUserRating = in.readString();
        mSynopsis = in.readString();
    }

    public String getPosterPath() {
        return mPosterPath;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mUserRating);
        dest.writeString(mSynopsis);
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
