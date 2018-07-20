package com.dhruvam.popularmovies.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.database.entity.MovieEntity;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntity>> movieList;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        movieList = OfflineMovieAccessDatabase.getInstance(application.getApplicationContext()).getMovieDao().getAllMovies();
    }

    public LiveData<List<MovieEntity>> getAllMovieList() {
        return movieList;
    }
}
