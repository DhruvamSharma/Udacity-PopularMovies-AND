package com.dhruvam.popularmovies.view_model;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;

public class FavouriteMovieViewModelById extends ViewModel {

    private LiveData<FavouriteMovies> favouriteMovie;

    public FavouriteMovieViewModelById(OfflineMovieAccessDatabase mDb, int movieId) {

        favouriteMovie = mDb.getDao().getMovieByIdLiveData(movieId);
    }

    public LiveData<FavouriteMovies> getFavouriteMovieById() {
        return favouriteMovie;
    }
}
