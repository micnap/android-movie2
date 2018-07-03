package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Displays details of a single movie.
 */
public class DetailActivity extends AppCompatActivity {

    private TextView mTitle;
    private ImageView mImage;
    private TextView mPlot;
    private TextView mRating;
    private TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.tv_title);
        mImage = (ImageView) findViewById(R.id.tv_image);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);

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
