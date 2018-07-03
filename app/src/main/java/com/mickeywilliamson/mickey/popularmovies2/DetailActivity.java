package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays details of a single movie.
 */
public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        // Get the movie data passed in from the Main screen and display it.
        final Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra("movie");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_nav_detail_data:
                        selectedFragment = DetailMain.newInstance(movie);
                        break;
                    case R.id.bottom_nav_detail_trailers:
                        selectedFragment = DetailTrailers.newInstance();
                        break;
                    case R.id.bottom_nav_detail_reviews:
                        selectedFragment = DetailReviews.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        // Manually displaying the first fragment.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, DetailMain.newInstance(movie));
        transaction.commit();

    }
}
