package com.eliassilva.popularmovies.reviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eliassilva.popularmovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Elias on 06/03/2018.
 */

/**
 * Provides access to the review data
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<ReviewPOJO> mReviewsList;
    final private ReviewAdapterOnClickHandler mClickHandler;

    public ReviewAdapter(ReviewAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    public interface ReviewAdapterOnClickHandler {
        void seeReview(String reviewUrl);
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int reviewItemId = R.layout.movie_review_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(reviewItemId, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        TextView reviewAuthor = holder.mReviewAuthor;
        TextView reviewContent = holder.mReviewContent;
        String currentReviewAuthor = mReviewsList.get(position).getReviewAuthor();
        String currentReviewContent = mReviewsList.get(position).getReviewContent();
        SpannableString underlineAuthor = new SpannableString(currentReviewAuthor);
        underlineAuthor.setSpan(new UnderlineSpan(), 0, underlineAuthor.length(), 0);
        reviewAuthor.setText(underlineAuthor);
        reviewContent.setText(currentReviewContent);
    }

    @Override
    public int getItemCount() {
        if (mReviewsList == null) {
            return 0;
        }
        return mReviewsList.size();
    }

    public void setReviewData(List<ReviewPOJO> reviews) {
        mReviewsList = reviews;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.review_author_tv)
        TextView mReviewAuthor;
        @BindView(R.id.review_content_tv)
        TextView mReviewContent;

        public ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ReviewPOJO reviewClicked = mReviewsList.get(getAdapterPosition());
            String reviewUrl = reviewClicked.getReviewUrl();
            mClickHandler.seeReview(reviewUrl);
        }
    }
}
