package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base activity for displaying details of a movie which includes a separate screen for data,
 * trailers, and reviews.
 */
public class DetailActivity extends AppCompatActivity {

    // Movie details screen is comprised of 3 fragments - details, trailers, and reviews.
    // BottomNavigationView is used to navigate between the three fragments.
    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        // Get the movie data passed in from the Main screen as an intent and display it.
        final Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra("movie");

        // Choose what fragment to display based on which tab in the BottomNavigationView that
        // was clicked on.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_nav_detail_data:
                        selectedFragment = DetailMain.newInstance(movie);
                        break;
                    case R.id.bottom_nav_detail_trailers:
                        selectedFragment = DetailTrailers.newInstance(movie);
                        break;
                    case R.id.bottom_nav_detail_reviews:
                        selectedFragment = DetailReviews.newInstance(movie);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        // Manually display the main details fragment on first visit to the activity.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, DetailMain.newInstance(movie));
        transaction.commit();
    }

    // Needed to maintain selected BottomNavigationView item across rotation.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("SelectedItemId", bottomNavigationView.getSelectedItemId());
    }

    // Needed to maintain selected BottomNavigationView item across rotation.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int selectedItemId = savedInstanceState.getInt("SelectedItemId");
        bottomNavigationView.setSelectedItemId(selectedItemId);
    }
}
