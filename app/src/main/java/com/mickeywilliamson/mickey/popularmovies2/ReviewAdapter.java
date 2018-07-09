package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private ArrayList<Review> reviews;

    // Constructor.
    public ReviewAdapter() {}

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutId = R.layout.review_list;
        View view = inflater.inflate(layoutId, parent, false);
        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        final Review currentReview = reviews.get(position);

        holder.mReview.setText(currentReview.getReview());
        holder.mAuthor.setText("- " + currentReview.getAuthor());
    }

    @Override
    public int getItemCount() {
        if (reviews == null) {
            return 0;
        } else  {
            return reviews.size();
        }
    }

    public void setReviewData(ArrayList<Review> reviewList) {
        reviews = reviewList;
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author) TextView mAuthor;
        @BindView(R.id.review) TextView mReview;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
