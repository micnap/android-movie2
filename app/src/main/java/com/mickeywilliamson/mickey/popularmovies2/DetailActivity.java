package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays details of a single movie.
 */
public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.tv_image) ImageView mImage;
    @BindView(R.id.tv_plot) TextView mPlot;
    @BindView(R.id.tv_rating) TextView mRating;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // Get the movie data passed in from the Main screen and display it.
        Intent intent = getIntent();
        if (intent.hasExtra("movie")) {
            Movie movie = intent.getParcelableExtra("movie");
            mTitle.setText(movie.getTitle());
            Picasso.with(this).load(MovieAdapter.getImagePath(movie.getImage(), MovieAdapter.WIDTH_W342)).placeholder(R.drawable.default_movie).error(R.drawable.default_movie).into(mImage);
            mImage.setContentDescription(movie.getTitle());
            mPlot.setText(movie.getPlot());
            String ratingText = getString(R.string.rating, movie.getRating());
            mRating.setText(ratingText);
            mReleaseDate.setText(MovieAdapter.getYear(movie.getReleaseDate()));
        }
    }
}
