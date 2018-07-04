package com.mickeywilliamson.mickey.popularmovies2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Utility class for parsing the JSON responses from the Movie API.
 */
final class JsonUtils {

    public static ArrayList<Movie> parseMoviesFromJSON(String moviesJsonString) throws JSONException {

        // The movie fields we're interested in.
        final String FIELD_ID = "id";
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
            movie.setId(results.getJSONObject(i).getString(FIELD_ID));
            movie.setTitle(results.getJSONObject(i).getString(FIELD_TITLE));
            movie.setImage(results.getJSONObject(i).getString(FIELD_IMAGE));
            movie.setPlot(results.getJSONObject(i).getString(FIELD_PLOT));
            movie.setRating(results.getJSONObject(i).getString(FIELD_RATING));
            movie.setReleaseDate(results.getJSONObject(i).getString(FIELD_RELEASE_DATE));
            movies.add(movie);
        }

        return movies;
    }

    public static ArrayList<Review> parseReviewsFromJSON(String reviewsJsonString) throws JSONException {

        Log.d("RESULTS", reviewsJsonString);

        // The review fields we're interested in.
        final String FIELD_AUTHOR = "author";
        final String FIELD_REVIEW = "content";

        // The parsed reviews will be stored in an ArrayList.
        ArrayList<Review> reviews = new ArrayList<>();

        // Create a JSON object from the JSON string returned by the API.
        JSONObject reviewsJson = new JSONObject(reviewsJsonString);

        // The movies are housed in the "results" portion of the response.
        JSONArray results = reviewsJson.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            Review review = new Review();

            review.setAuthor(results.getJSONObject(i).getString(FIELD_AUTHOR));
            review.setReview(results.getJSONObject(i).getString(FIELD_REVIEW));
            reviews.add(review);
        }
        Log.d("COUNT", String.valueOf(reviews.size()));
        return reviews;
    }

    public static ArrayList<Trailer> parseTrailersFromJSON(String trailersJsonString) throws JSONException {

        Log.d("RESULTS", trailersJsonString);

        // The review fields we're interested in.
        final String FIELD_TITLE = "name";
        final String FIELD_VIDEO_ID = "key";

        // The parsed reviews will be stored in an ArrayList.
        ArrayList<Trailer> trailers = new ArrayList<>();

        // Create a JSON object from the JSON string returned by the API.
        JSONObject trailersJson = new JSONObject(trailersJsonString);

        // The movies are housed in the "results" portion of the response.
        JSONArray results = trailersJson.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            Trailer trailer = new Trailer();

            trailer.setTitle(results.getJSONObject(i).getString(FIELD_TITLE));
            trailer.setVideoID(results.getJSONObject(i).getString(FIELD_VIDEO_ID));
            trailers.add(trailer);
        }
        Log.d("TRAILER COUNT", String.valueOf(results.length()));
        return trailers;
    }
}
