package com.dhruvam.popularmovies.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;

public class FavouriteMovieByIdViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    OfflineMovieAccessDatabase mDb;
    int movieId;

    public FavouriteMovieByIdViewModelFactory(OfflineMovieAccessDatabase mDb, int id) {

        this.mDb = mDb;
        this.movieId = id;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return  (T) new FavouriteMovieViewModelById( mDb, movieId);
    }
}
