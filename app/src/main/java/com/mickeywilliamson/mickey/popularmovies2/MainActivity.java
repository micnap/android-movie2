package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    // Uses ButterKnife annotations to bind views to variables.
    @BindView(R.id.rv_movie_list) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pb_loader) ProgressBar mLoader;

    private MovieAdapter mMovieAdapter;

    private static final int MOVIES_LOADER = 2450;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Create the grid view.
        GridLayoutManager lm = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);

        // Attach the data to the grid.
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        // Load the movie data.
        String sort = MovieGlobals.SORT != null ? MovieGlobals.SORT : MovieAdapter.SORT_POPULAR;
        Bundle bundle = new Bundle();
        bundle.putString("sort", sort);
        loadMovies(bundle);
    }

    /**
     * Loads the movie data in a background thread.
     *
     * @param bundle Bundle
     *        bundle holds the Sort that movies should be shown in (most popular or top rated).
     */
    protected void loadMovies(Bundle bundle) {

        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        getSupportLoaderManager().initLoader(MOVIES_LOADER, bundle, this);
    }

    /**
     * Utility method to hide the recycler view and display an error if we can't get the data.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Opens the detail view of the movie that was clicked on.
     *
     * @param clickedMovie Movie
     *      The movie object the user clicked on in the list.
     */
    @Override
    public void onListItemClick(Movie clickedMovie) {

        // Packs up the movie data of the movie that was clicked and passes it to the Detail activity.
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie", clickedMovie);
        startActivity(intent);
    }

    // Movies are retrieved on a background thread.
    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            ArrayList<Movie> mMovies;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                mLoader.setVisibility(View.VISIBLE);

                // If results are cached, return those.  Otherwie, fetch new results.
                if (mMovies != null) {
                    deliverResult(mMovies);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public ArrayList<Movie> loadInBackground() {

                // The string that determines the endpoint from which to get the movie data.
                String sort = args.getString("sort");

                // Build the endpoint url.
                URL url = NetworkUtils.buildUrl(MainActivity.this, sort);

                // Attempt to get the movie data and parse it.
                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                    mMovies = JsonUtils.parseMoviesFromJSON(jsonResponse);

                    return mMovies;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // Cache results.
            @Override
            public void deliverResult(ArrayList<Movie> movies) {
                mMovies = movies;
                super.deliverResult(movies);
            }
        };
    }

    // Movies have been retrieved.  And now we display them on the UI thread.
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {

        // if we successfully retrieved the movie data, hide the loader icon and display the movies.
        // Otherwise, display the error.
        mLoader.setVisibility(View.INVISIBLE);
        if (data != null) {
            mErrorMessage.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mMovieAdapter.setMovieData(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Display a menu that allows a person to choose how they want the movies sorted.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // If the "popular" sort is chosen, persist the choice and display the appropriate movies.
        if (id == R.id.sort_popular) {
            return chooseSort(MovieAdapter.SORT_POPULAR);
        }

        // If the "top rated" sort is chosen, persist the choice and display the appropriate movies.
        if (id == R.id.sort_top_rated) {
            return chooseSort(MovieAdapter.SORT_TOP_RATED);
        }

        if (id == R.id.sort_favorites) {
            Intent intent = new Intent(this, FavoriteMoviesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Utility method for persisting and executing the sort choice.
     * @param sort String
     *             The type of sort.
     *
     * @return boolean
     *              Returns true
     */
    public boolean chooseSort(String sort) {
        MovieGlobals.SORT = sort;
        Bundle bundle = new Bundle();
        bundle.putString("sort", sort);
        loadMovies(bundle);
        return true;
    }
}
