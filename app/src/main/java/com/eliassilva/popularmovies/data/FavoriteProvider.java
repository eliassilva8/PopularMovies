package com.eliassilva.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.eliassilva.popularmovies.data.FavoritesContract.FavoriteEntry;

/**
 * Created by Elias on 08/03/2018.
 */

/**
 * Content provider for Favorites data
 */
public class FavoriteProvider extends ContentProvider {
    private int rowsUpdated;
    private int rowsDeleted;
    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY, FavoritesContract.PATH_FAVORITES, MOVIES);
        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY, FavoritesContract.PATH_FAVORITES + "#", MOVIE_ID);
    }

    public static final String LOG_TAG = FavoriteProvider.class.getSimpleName();

    private FavoritesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                cursor = database.query(FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_ID:
                selection = FavoriteEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return insertMovie(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a movie into the favorites database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertMovie(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(FavoriteEntry.TABLE_NAME, null, values);

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        return rowsDeleted = database.delete(FavoriteEntry.TABLE_NAME, selection, selectionArgs);

        /*final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                rowsDeleted = database.delete(FavoriteEntry.TABLE_NAME, selection, selectionArgs);
            case MOVIE_ID:
                //selection = FavoriteEntry.COLUMN_ID + "=?";
                //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(FavoriteEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }*/
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return updatePet(uri, values, selection, selectionArgs);
            case MOVIE_ID:
                selection = FavoriteEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update movies in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more movies).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        rowsUpdated = database.update(FavoriteEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return rowsDeleted;
        }
        return rowsUpdated;
    }
}
