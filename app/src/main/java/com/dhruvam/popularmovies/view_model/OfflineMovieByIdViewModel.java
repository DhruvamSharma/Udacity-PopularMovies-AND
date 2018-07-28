package com.dhruvam.popularmovies.view_model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.MovieEntity;

public class OfflineMovieByIdViewModel extends ViewModel{


    private LiveData<MovieEntity> movieEntityLiveData;

    public OfflineMovieByIdViewModel(OfflineMovieAccessDatabase mDatabase, int mFavouriteMovieId) {
        Log.e("in view model", "fetching from database");
        movieEntityLiveData = mDatabase.getMovieDao().getMovieById(mFavouriteMovieId);
    }

    public LiveData<MovieEntity> getMovieById() {
        return movieEntityLiveData;
    }


}
