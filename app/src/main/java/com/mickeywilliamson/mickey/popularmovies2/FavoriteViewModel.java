package com.mickeywilliamson.mickey.popularmovies2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.mickeywilliamson.mickey.popularmovies2.Database.AppDatabase;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> favoriteMovies;

    public FavoriteViewModel(Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        favoriteMovies = db.favoriteMoviesDao().loadAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies() { return favoriteMovies; }
}
