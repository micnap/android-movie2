package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Intent;
import android.os.AsyncTask;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessage;
    private ProgressBar mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mLoader = (ProgressBar) findViewById(R.id.pb_loader);

        // Create the grid view.
        GridLayoutManager lm = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);

        // Attach the data to the grid.
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        // Load the movie data.
        loadMovies();
    }

    /**
     * Loads the movie data in a background thread.
     */
    private void loadMovies() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        // The parameter of the execute method determines the sort (popular/top rated) that is to be
        // displayed. If no sort has been previously set, retrieve the movies sorted by popular.
        // Otherwise, show the sort previously chosen.
        new MovieAsyncTask().execute(MovieGlobals.SORT != null ? MovieGlobals.SORT : MovieAdapter.SORT_POPULAR);
    }

    /**
     * Utility method to hide the recycler view and display an error if we can't get the data.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(Movie clickedMovie) {

        // Packs up the movie data of the movie that was clicked and passes it to the Detail activity.
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie", clickedMovie);
        startActivity(intent);
    }

    /**
     * Retrieves the movie data from the appropriate endpoint
     */
    class MovieAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Shows an animated loader icon while the data is loading.
            mLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {

            ArrayList<Movie> movies;

            // The string that determines the endpoint from which to get the movie data.
            String sort = strings[0];

            // Build the endpoint url.
            URL url = NetworkUtils.buildUrl(MainActivity.this, sort);

            // Attempt to get the movie data and parse it.
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                movies = JsonUtils.parseMoviesFromJSON(jsonResponse);

                return movies;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            // if we successfully retrieved the movie data, hide the loader icon and display the movies.
            // Otherwise, display the error.
            mLoader.setVisibility(View.INVISIBLE);
            if (movies != null) {
                mErrorMessage.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mMovieAdapter.setMovieData(movies);
            } else {
                showErrorMessage();
            }
        }
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
    private boolean chooseSort(String sort) {
        MovieGlobals.SORT = sort;
        new MovieAsyncTask().execute(sort);
        return true;
    }
}
