package com.mickeywilliamson.mickey.popularmovies2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Utility class for parsing the JSON response from the Movie API.
 */
final class JsonUtils {

    public static ArrayList<Movie> parseMoviesFromJSON(String moviesJsonString) throws JSONException {

        // The movie fields we're interested in.
        final String FIELD_TITLE = "title";
        final String FIELD_IMAGE = "poster_path";
        final String FIELD_PLOT = "overview";
        final String FIELD_RATING = "vote_average";
        final String FIELD_RELEASE_DATE = "release_date";

        // The parsed movies will be stored in an ArrayList.
        ArrayList<Movie> movies = new ArrayList<>();

        // Create a JSON object from the JSON string returned by the API.
        JSONObject moviesJson = new JSONObject(moviesJsonString);

        // The movies are housed in the "results" portion of the response.
        JSONArray results = moviesJson.getJSONArray("results");

        // Extract the movie data and store it in the arraylist.
        for (int i = 0; i < results.length(); i++) {
            Movie movie = new Movie();
            movie.setTitle(results.getJSONObject(i).getString(FIELD_TITLE));
            movie.setImage(results.getJSONObject(i).getString(FIELD_IMAGE));
            movie.setPlot(results.getJSONObject(i).getString(FIELD_PLOT));
            movie.setRating(results.getJSONObject(i).getString(FIELD_RATING));
            movie.setReleaseDate(results.getJSONObject(i).getString(FIELD_RELEASE_DATE));
            movies.add(movie);
        }

        return movies;
    }
}
