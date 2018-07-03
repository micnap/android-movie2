package com.mickeywilliamson.mickey.popularmovies2;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailMain extends Fragment {

    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.tv_image) ImageView mImage;
    @BindView(R.id.tv_plot) TextView mPlot;
    @BindView(R.id.tv_rating) TextView mRating;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;

    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_PLOT = "plot";
    private static final String ARG_RATING = "rating";
    private static final String ARG_DATE = "date";

    // Required empty public constructor.
    public DetailMain() {}

    public static DetailMain newInstance(Movie movie) {
        DetailMain fragment = new DetailMain();
        Bundle args = new Bundle();
        args.putString(ARG_ID, movie.getId());
        args.putString(ARG_TITLE, movie.getTitle());
        args.putString(ARG_IMAGE, movie.getImage());
        args.putString(ARG_PLOT, movie.getPlot());
        args.putString(ARG_RATING, movie.getRating());
        args.putString(ARG_DATE, movie.getReleaseDate());
        fragment.setArguments(args);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_main, container, false);
        ButterKnife.bind(this, view);

        // Get the movie data passed in from the Main screen and display it.
        if (getArguments() != null) {
            mTitle.setText(getArguments().getString(ARG_TITLE));
            Picasso.with(getActivity()).load(MovieAdapter.getImagePath(getArguments().getString(ARG_IMAGE), MovieAdapter.WIDTH_W342)).placeholder(R.drawable.default_movie).error(R.drawable.default_movie).into(mImage);
            mImage.setContentDescription(getArguments().getString(ARG_TITLE));
            mPlot.setText(getArguments().getString(ARG_PLOT));
            String ratingText = getString(R.string.rating, getArguments().getString(ARG_RATING));
            mRating.setText(ratingText);
            mReleaseDate.setText(MovieAdapter.getYear(getArguments().getString(ARG_DATE)));
        }

        return view;
    }
}
