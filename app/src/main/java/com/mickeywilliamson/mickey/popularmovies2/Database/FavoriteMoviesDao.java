package com.mickeywilliamson.mickey.popularmovies2.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mickeywilliamson.mickey.popularmovies2.Movie;

import java.util.List;

/**
 * Data access objects for CRUD operations.
 */
@Dao
public interface FavoriteMoviesDao {

    @Query("SELECT * FROM " + AppDatabase.DATABASE_NAME)
    LiveData<List<Movie>> loadAllFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(Movie movie);

    @Delete
    void deleteFavoriteMovie(Movie movie);

    @Query("SELECT * FROM " + AppDatabase.DATABASE_NAME + " WHERE id = :id")
    LiveData<Movie> loadFavoriteMovieById(String id);
}
