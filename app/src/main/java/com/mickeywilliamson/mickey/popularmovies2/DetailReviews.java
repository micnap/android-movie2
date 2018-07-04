package com.mickeywilliamson.mickey.popularmovies2;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailReviews extends Fragment {

    @BindView(R.id.rv_review_list) RecyclerView mReviewList;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pb_loader) ProgressBar mLoader;

    private static final String PATH = "reviews";
    private static final String ARG_ID = "id";
    private ReviewAdapter mReviewAdapter;

    // Required empty public constructor
    public DetailReviews() {}

    public static DetailReviews newInstance(String id) {
        DetailReviews fragment = new DetailReviews();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mReviewList.setLayoutManager(lm);

        mReviewAdapter = new ReviewAdapter();
        mReviewList.setAdapter(mReviewAdapter);
    }

    /**
     * Loads the movie data in a background thread.
     */
    private void loadReviews(String id) {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mReviewList.setVisibility(View.VISIBLE);

        // The parameter of the execute method determines the sort (popular/top rated) that is to be
        // displayed. If no sort has been previously set, retrieve the movies sorted by popular.
        // Otherwise, show the sort previously chosen.


        new ReviewAsyncTask().execute(id);

    }

    /**
     * Utility method to hide the recycler view and display an error if we can't get the data.
     */
    private void showErrorMessage(String message) {
        mReviewList.setVisibility(View.INVISIBLE);
        if (message != null) {
            mErrorMessage.setText(message);
        }
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_reviews, container, false);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            loadReviews(getArguments().getString(ARG_ID));
        }

        return view;
    }

    /**
     * Retrieves the movie data from the appropriate endpoint
     */
    class ReviewAsyncTask extends AsyncTask<String, Void, ArrayList<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Shows an animated loader icon while the data is loading.
            mLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Review> doInBackground(String... strings) {

            ArrayList<Review> reviews;

            // The string that determines the endpoint from which to get the movie data.
            String id = strings[0];

            // Build the endpoint url.
            URL url = NetworkUtils.buildUrl(getActivity(), id + "/" + PATH);

            // Attempt to get the movie data and parse it.
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                reviews = JsonUtils.parseReviewsFromJSON(jsonResponse);

                return reviews;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {

            // if we successfully retrieved the movie data, hide the loader icon and display the movies.
            // Otherwise, display the error.
            mLoader.setVisibility(View.INVISIBLE);
            if (reviews.size() == 0) {
                showErrorMessage(getString(R.string.empty));
            } else if (reviews != null) {
                mErrorMessage.setVisibility(View.INVISIBLE);
                mReviewList.setVisibility(View.VISIBLE);
                mReviewAdapter.setReviewData(reviews);
            } else {
                showErrorMessage(null);
            }
        }
    }

}
