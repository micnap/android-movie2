package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mickeywilliamson.mickey.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movie> movies;
    private final ListItemClickListener mOnClickListener;

    // Constants used in building endpoint url.
    private static final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/";
    public static final String SORT_POPULAR = "popular";
    public static final String SORT_TOP_RATED = "top_rated";

    // The choices that the Movie API gives us for image widths.
    // Only 2 of these are currently in use.
    public static final String WIDTH_W92 = "w92";
    public static final String WIDTH_W154 = "w154";
    public static final String WIDTH_W185 = "w185";
    public static final String WIDTH_W342 = "w342";
    public static final String WIDTH_W500 = "w500";
    public static final String WIDTH_W780 = "w780";
    public static final String WIDTH_ORIGINAL = "original";

    // Constructor.
    public MovieAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutId = R.layout.movie_list;
        View view = inflater.inflate(layoutId, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {

        final Movie currentMovie = movies.get(position);

        // Create the view's image path and load the image with Picasso.
        String image_path = getImagePath(currentMovie.getImage(), WIDTH_W500);
        Picasso.with(holder.mImage.getContext()).load(image_path).into(holder.mImage);
        holder.mImage.setContentDescription(currentMovie.getTitle());
    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        } else {
            return movies.size();
        }
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mImage;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.iv_image);
            mImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(movies.get(clickedPosition));
        }
    }

    /**
     * Updates the movie data.
     *
     * @param movieList ArrayList
     *         List of movie data.
     */
    public void setMovieData(ArrayList<Movie> movieList) {
        movies = movieList;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(Movie clickedMovie);
    }

    /**
     * Utility function that builds the path for the image.
     *
     * @param fileName String
     *        The image's file name.
     * @param width
     *        The width the image should be displayed at.
     *
     * @return String
     *        Returns the absolute path of the image.
     */
    public static String getImagePath(String fileName, String width) {
        return IMAGE_BASE_PATH + width + fileName;
    }

    /**
     * Utility function for extracting the year from the movie's release date.
     *
     * @param dateString String
     *        The release date of the movie in yyyy-MM-dd form.
     *
     * @return String
     *        The four digit year the movie was release.
     */
    public static String getYear(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date parsedDate = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(parsedDate);

            return String.valueOf(cal.get(Calendar.YEAR));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
