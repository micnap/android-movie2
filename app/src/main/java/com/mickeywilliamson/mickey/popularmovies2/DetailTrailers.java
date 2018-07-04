package com.mickeywilliamson.mickey.popularmovies2;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTrailers extends Fragment implements TrailerAdapter.ListItemClickListener {

    @BindView(R.id.rv_trailer_list) RecyclerView mTrailerList;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pb_loader) ProgressBar mLoader;

    private static final String PATH = "videos";
    private static final String ARG_ID = "id";
    private TrailerAdapter mTrailerAdapter;

    // Required empty public constructor
    public DetailTrailers() {}

    public static DetailTrailers newInstance(String id) {
        DetailTrailers fragment = new DetailTrailers();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
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
     * Loads the movie data in a background thread.
     */
    private void loadTrailers(String id) {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mTrailerList.setVisibility(View.VISIBLE);

        // The parameter of the execute method determines the sort (popular/top rated) that is to be
        // displayed. If no sort has been previously set, retrieve the movies sorted by popular.
        // Otherwise, show the sort previously chosen.


        new DetailTrailers.TrailerAsyncTask().execute(id);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_trailers, container, false);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            loadTrailers(getArguments().getString(ARG_ID));
        }

        return view;
    }

    // Courtesy of https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent.
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


    /**
     * Retrieves the movie data from the appropriate endpoint
     */
    class TrailerAsyncTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Shows an animated loader icon while the data is loading.
            mLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... strings) {

            ArrayList<Trailer> trailers;

            // The string that determines the endpoint from which to get the movie data.
            String id = strings[0];

            // Build the endpoint url.
            URL url = NetworkUtils.buildUrl(getActivity(), id + "/" + PATH);

            // Attempt to get the movie data and parse it.
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                trailers = JsonUtils.parseTrailersFromJSON(jsonResponse);

                return trailers;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {

            // if we successfully retrieved the movie data, hide the loader icon and display the movies.
            // Otherwise, display the error.
            mLoader.setVisibility(View.INVISIBLE);
            if (trailers.size() == 0) {
                showErrorMessage(getString(R.string.empty));
            } else if (trailers != null) {
                mErrorMessage.setVisibility(View.INVISIBLE);
                mTrailerList.setVisibility(View.VISIBLE);
                mTrailerAdapter.setTrailerData(trailers);
            } else {
                showErrorMessage(null);
            }
        }
    }

}
