package com.mickeywilliamson.mickey.popularmovies2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mickeywilliamson.mickey.popularmovies2.Database.AppDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteMoviesActivity extends AppCompatActivity implements FavoriteAdapter.ItemClickListener  {

    @BindView(R.id.rv_favorite_list) RecyclerView mFavoriteList;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pb_loader) ProgressBar mLoader;

    private FavoriteAdapter mFavoriteAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        ButterKnife.bind(this);

        setTitle(getString(R.string.favorite_movies));

        mFavoriteList.setLayoutManager(new LinearLayoutManager(this));
        mFavoriteAdapter = new FavoriteAdapter(this, this);
        mFavoriteList.setAdapter(mFavoriteAdapter);
        mDb = AppDatabase.getInstance(getApplicationContext());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Movie> favoriteMovies = mFavoriteAdapter.getFavoriteMovies();
                        mDb.favoriteMoviesDao().deleteFavoriteMovie(favoriteMovies.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mFavoriteList);

        setupViewModel();

    }

    private void setupViewModel() {
        FavoriteViewModel viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {

                mFavoriteAdapter.setFavoriteMovies(movies);
            }
        });
    }

    @Override
    public void onItemClickListener(Movie movie) {
        // Launch AddTaskActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(FavoriteMoviesActivity.this, DetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}
