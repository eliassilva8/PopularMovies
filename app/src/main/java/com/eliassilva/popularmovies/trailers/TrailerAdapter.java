package com.eliassilva.popularmovies.trailers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eliassilva.popularmovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Elias on 28/02/2018.
 */

/**
 * Provides access to the trailer data
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<TrailerPOJO> mTrailersList;
    final private TrailerAdapterOnClickHandler mClickHandler;

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    public interface TrailerAdapterOnClickHandler {
        void playTrailer(String key);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int trailerItemId = R.layout.movie_trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(trailerItemId, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        TextView trailerName = holder.mTrailerName;
        String currentTrailerName = mTrailersList.get(position).getTrailerName();
        trailerName.setText(currentTrailerName);
    }

    @Override
    public int getItemCount() {
        if (mTrailersList == null) {
            return 0;
        }
        return mTrailersList.size();
    }

    public void setTrailerData(List<TrailerPOJO> trailers) {
        mTrailersList = trailers;
        notifyDataSetChanged();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.trailer_name_tv)
        TextView mTrailerName;
        @BindView(R.id.play_video_bt)
        ImageView mPlayTrailer;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPlayTrailer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TrailerPOJO trailerClicked = mTrailersList.get(getAdapterPosition());
            String key = trailerClicked.getTrailerKey();
            mClickHandler.playTrailer(key);
        }
    }
}
