package com.eliassilva.popularmovies.data;

/**
 * Created by Elias on 12/03/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.eliassilva.popularmovies.R;

/**
 * Create list items for each row of favorite data in the cursor
 */
public class FavoriteCursorAdapter extends CursorAdapter {
    public FavoriteCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_poster_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
