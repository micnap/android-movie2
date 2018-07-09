package com.mickeywilliamson.mickey.popularmovies2;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mickeywilliamson.mickey.popularmovies2.Database.AppDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This is the main movie detail fragment. It displays a movie's details.
 */
public class DetailMain extends Fragment {

    // Binds views to variables using ButterKnife library.
    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.tv_image) ImageView mImage;
    @BindView(R.id.tv_plot) TextView mPlot;
    @BindView(R.id.tv_rating) TextView mRating;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.btn_favorite) Button btnFavorite;

    // The movie's data field names.
    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_PLOT = "plot";
    private static final String ARG_RATING = "rating";
    private static final String ARG_DATE = "date";

    // The variable that holds a reference to the app's database from which favorite movies are stored.
    private AppDatabase mDb;

    // Required empty public constructor.
    public DetailMain() {}

    // Instantiates the fragment, getting the movie details passed in.  The details are passed first from
    // the click on the movie in the list of movies (MainActivity) to the DetailActivity in the form
    // of an intent.  They are then passed from the detail activity to this fragment in a bundle.
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

    // Sets the views' values from the data passed into the fragment.
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

            // If this movie is already a "favorite", change the button's text to reflect this.
            // If it's not already a favorite and the button is clicked, its text is automatically
            // set to reflect it's been added as a favorite.
            mDb = AppDatabase.getInstance(getActivity());
            final LiveData<Movie> favMovie = mDb.favoriteMoviesDao().loadFavoriteMovieById(getArguments().getString(ARG_ID));
            favMovie.observe(getActivity(), new Observer<Movie>() {

                @Override
                public void onChanged(@Nullable Movie movie) {
                    if (movie != null) {
                        btnFavorite.setText(getString(R.string.in_favorites));
                        btnFavorite.setEnabled(false);
                    }
                }
            });
        }

        return view;
    }

    // When the favorite button is clicked, the movie is added to the favorite_movie database
    // and a Toast is shown confirming the addition.
    @OnClick(R.id.btn_favorite)
    public void markFavorite(View view) {
        mDb = AppDatabase.getInstance(getActivity());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteMoviesDao().insertFavoriteMovie(
                    new Movie(
                            getArguments().getString(ARG_ID),
                            getArguments().getString(ARG_TITLE),
                            getArguments().getString(ARG_IMAGE),
                            getArguments().getString(ARG_PLOT),
                            getArguments().getString(ARG_RATING),
                            getArguments().getString(ARG_DATE)
                    )
                );
            }
        });

        Toast.makeText(getActivity(), "Added to Favorites", Toast.LENGTH_SHORT).show();
    }
}
