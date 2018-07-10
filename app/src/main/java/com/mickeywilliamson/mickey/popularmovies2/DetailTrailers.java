package com.mickeywilliamson.mickey.popularmovies2;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
 * This class displays the movie's trailers as a fragment attached to the DetailActivity.
 */
public class DetailTrailers extends Fragment implements TrailerAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {

    // Binds views to variables using ButterKnife library.
    @BindView(R.id.rv_trailer_list) RecyclerView mTrailerList;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pb_loader) ProgressBar mLoader;
    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.linearlayout) LinearLayout ll;

    private static final String PATH = "videos";
    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";
    private static final int MOVIE_TRAILERS_LOADER = 2452;
    private TrailerAdapter mTrailerAdapter;

    // Required empty public constructor
    public DetailTrailers() {}

    // Instantiates the fragment and extracts the id and title of the movie passed in.
    public static DetailTrailers newInstance(Movie movie) {
        DetailTrailers fragment = new DetailTrailers();
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
        mTrailerList.setLayoutManager(lm);

        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerList.setAdapter(mTrailerAdapter);
    }

    /**
     * Loads the movie trailers in an asynctaskloader from the endpoint
     * http://api.themoviedb.org/3/movie/[id]/videos?api_key=[api_key] using the movie's id that
     * was passed into the class.  The list of reviews retrieved is then cached.
     */
    private void loadTrailers(String id) {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mTrailerList.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        //getActivity().getSupportLoaderManager().initLoader(MOVIE_TRAILERS_LOADER, bundle, this);

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<String> movieTrailersLoader = loaderManager.getLoader(MOVIE_TRAILERS_LOADER);
        if (movieTrailersLoader == null) {
            loaderManager.initLoader(MOVIE_TRAILERS_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_TRAILERS_LOADER, bundle, this);
        }
        //new DetailTrailers.TrailerAsyncTask().execute(id);

    }

    /**
     * Utility method to hide the recycler view and display an error if we can't get the data.
     */
    private void showErrorMessage(String message) {
        mTrailerList.setVisibility(View.INVISIBLE);
        if (message != null) {
            mErrorMessage.setText(message);
        }
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    // Binds the views used in the fragment, sets the movie's title for display and kicks off the
    // retrieving of trailers.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_trailers, container, false);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            loadTrailers(getArguments().getString(ARG_ID));
            mTitle.setText(getArguments().getString(ARG_TITLE));
        }


        return view;
    }

    // Courtesy of https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent.
    // Launches the trailer either in the YouTube native app (if available) or in a browser.
    @Override
    public void onListItemClick(Trailer clickedTrailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + clickedTrailer.getVideoId()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + clickedTrailer.getVideoId()));
        try {
            getActivity().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            getActivity().startActivity(webIntent);
        }
    }

    // Reviews are retrieved asynchronously.
    @NonNull
    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int id, @Nullable final Bundle args) {

        return new AsyncTaskLoader<ArrayList<Trailer>>(getActivity()) {

            ArrayList<Trailer> mTrailers;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                mLoader.setVisibility(View.VISIBLE);

                // If cached results exist, return those. Otherwise, fetch new results.
                if (mTrailers != null) {
                    deliverResult(mTrailers);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public ArrayList<Trailer> loadInBackground() {

                // The string that determines the endpoint from which to get the movie data.
                String id = args.getString("id");

                // Build the endpoint url.
                URL url = NetworkUtils.buildUrl(getActivity(), id + "/" + PATH);

                // Attempt to get the movie data and parse it.
                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                    mTrailers = JsonUtils.parseTrailersFromJSON(jsonResponse);

                    return mTrailers;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // Cache results.
            @Override
            public void deliverResult(ArrayList<Trailer> trailerList) {
                mTrailers = trailerList;
                super.deliverResult(trailerList);
            }
        };
    }

    // Trailers have been retrieved.  And now we display them.
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {

        // if we successfully retrieved the movie data, hide the loader icon and display the movies.
        // Otherwise, display the error.
        mLoader.setVisibility(View.INVISIBLE);

        // A hack: Setting the loader to invisible (above) just hides it from view but doesn't remove
        // the space it takes up in the UI.  This removes the space.
        ll.removeView(mLoader);

        // If data exists, hide the errormessage view and display the data.  Otherwise, show the error message.
        if (data.size() == 0) {
            showErrorMessage(getString(R.string.empty_trailers));
        } else if (data != null) {
            mErrorMessage.setVisibility(View.INVISIBLE);

            // A hack: Setting the errormessage to invisible just hides it from view but doesn't remove
            // the space it takes up in the UI.  This removes the space.
            ll.removeView(mErrorMessage);

            mTrailerList.setVisibility(View.VISIBLE);

            // Send the data to the recyclerview for display.
            mTrailerAdapter.setTrailerData(data);
        } else {
            showErrorMessage(null);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Trailer>> loader) {

    }

}
