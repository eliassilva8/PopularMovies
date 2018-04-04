package com.eliassilva.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.eliassilva.popularmovies.data.FavoritesContract.FavoriteEntry;

/**
 * Created by Elias on 08/03/2018.
 */

/**
 * Manages a local database for favorite movies data.
 */
public class FavoritesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 6;

    FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                        FavoriteEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        FavoriteEntry.COLUMN_POSTER_PATH + " TEXT, " +
                        FavoriteEntry.COLUMN_RATING + " TEXT, " +
                        FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                        FavoriteEntry.COLUMN_SYNOPSIS + " TEXT, " +
                        FavoriteEntry.COLUMN_IS_FAVORITE + " INTEGER NOT NULL DEFAULT 1, " +
                        " UNIQUE (" + FavoriteEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
