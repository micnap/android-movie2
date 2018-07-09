package com.mickeywilliamson.mickey.popularmovies2.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.mickeywilliamson.mickey.popularmovies2.Movie;

/**
 * Class for getting an instance of the database.
 * Uses singleton pattern so only one instance is used app-wide.
 */
@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "favorite_movies";

    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (new Object()) {
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME).build();
            }
        }

        return sInstance;
    }

    public abstract FavoriteMoviesDao favoriteMoviesDao();
}
