package com.mickeywilliamson.mickey.popularmovies2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * This class displays a movie's reviews as a fragment attached to the DetailActivity.
 */
public class DetailReviews extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

    // Binds views to variables using ButterKnife library.
    @BindView(R.id.rv_review_list) RecyclerView mReviewList;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pb_loader) ProgressBar mLoader;
    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.linearlayout) LinearLayout ll;

    private static final String PATH = "reviews";
    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";
    private ReviewAdapter mReviewAdapter;

    private static final int MOVIE_REVIEWS_LOADER = 2451;

    // Required empty public constructor
    public DetailReviews() {}

    // Instantiates the fragment and extracts the id and title of the movie passed in.
    public static DetailReviews newInstance(Movie movie) {
        DetailReviews fragment = new DetailReviews();
        Bundle args = new Bundle();
        args.putString(ARG_ID, movie.getId());
        args.putString(ARG_TITLE, movie.getTitle());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mReviewList.setLayoutManager(lm);
    }

    /**
     * Loads the movie reviews in an asynctaskloader from the endpoint
     * http://api.themoviedb.org/3/movie/[id]/reviews?api_key=[api_key] using the movie's id that
     * was passed into the class.  The list of reviews retrieved is then cached.
     */
    private void loadReviews(String id) {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mReviewList.setVisibility(View.VISIBLE);

        mReviewAdapter = new ReviewAdapter();
        mReviewList.setAdapter(mReviewAdapter);

        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<String> movieReviewsLoader = loaderManager.getLoader(MOVIE_REVIEWS_LOADER);
        if (movieReviewsLoader == null) {
            loaderManager.initLoader(MOVIE_REVIEWS_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_REVIEWS_LOADER, bundle, this);
        }
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

    // Binds the views used in the fragment, sets the movie's title for display and kicks off the
    // retrieving of reviews.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_reviews, container, false);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            loadReviews(getArguments().getString(ARG_ID));
            mTitle.setText(getArguments().getString(ARG_TITLE));
        }

        return view;
    }

    // Reviews are retrieved asynchronously.
    @NonNull
    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, @Nullable final Bundle args) {

        return new AsyncTaskLoader<ArrayList<Review>>(getActivity()) {

            ArrayList<Review> mReviews;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }

                mLoader.setVisibility(View.VISIBLE);

                // If results are cached, return those.  Otherwise, fetch new results.
                if (mReviews != null) {
                    deliverResult(mReviews);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public ArrayList<Review> loadInBackground() {

                // The string that determines the endpoint from which to get the movie data.
                String id = args.getString("id");

                // Build the endpoint url.
                URL url = NetworkUtils.buildUrl(getActivity(), id + "/" + PATH);

                // Attempt to get the movie data and parse it.
                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                    mReviews = JsonUtils.parseReviewsFromJSON(jsonResponse);

                    return mReviews;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // Cache results.
            @Override
            public void deliverResult(ArrayList<Review> reviewList) {
                mReviews = reviewList;
                super.deliverResult(reviewList);
            }
        };
    }

    // Reviews have been retrieved.  And now we display them on the UI thread.
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Review>> loader, ArrayList<Review> data) {

        // if we successfully retrieved the movie data, hide the loader icon and display the movies.
        // Otherwise, display the error.
        mLoader.setVisibility(View.INVISIBLE);

        // A hack: Setting the loader to invisible (above) just hides it from view but doesn't remove
        // the space it takes up in the UI.  This removes the space.
        ll.removeView(mLoader);

        // If data exists, hide the errormessage view and display the data.  Otherwise, show the error message.
        if (data.size() == 0) {
            showErrorMessage(getString(R.string.empty_reviews));
        } else if (data != null) {
            mErrorMessage.setVisibility(View.INVISIBLE);

            // A hack: Setting the errormessage to invisible just hides it from view but doesn't remove
            // the space it takes up in the UI.  This removes the space.
            ll.removeView(mErrorMessage);

            mReviewList.setVisibility(View.VISIBLE);

            // Send the data to the recyclerview for display.
            mReviewAdapter.setReviewData(data);
        } else {
            showErrorMessage(null);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Review>> loader) {

    }
}
