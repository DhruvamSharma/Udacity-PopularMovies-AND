package com.dhruvam.popularmovies.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.database.entity.MovieEntity;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntity>> movieList;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        Log.e("Actively retrieving", " from the database here");
        movieList = OfflineMovieAccessDatabase.getInstance(application.getApplicationContext()).getMovieDao().getAllMovies();
    }

    public LiveData<List<MovieEntity>> getAllMovieList() {
        return movieList;
    }

    public void setAllMovieList(LiveData<List<MovieEntity>> movieList) {
        this.movieList = movieList;
    }
}
