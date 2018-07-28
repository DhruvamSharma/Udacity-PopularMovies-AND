package com.dhruvam.popularmovies.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dhruvam.popularmovies.database.dao.FavouriteMovieDAO;
import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;

import java.util.List;

public class FavouriteMoviesViewModel extends AndroidViewModel {


    private LiveData<List<FavouriteMovies>> favouriteMovieList;

    public FavouriteMoviesViewModel(@NonNull Application application) {
        super(application);
        Log.e("in favourites", "fetching from database");
        favouriteMovieList = OfflineMovieAccessDatabase.getInstance(application.getApplicationContext()).getDao().getFavouriteMovieList();
    }

    public LiveData<List<FavouriteMovies>> getFavouriteMovieList() {
        return favouriteMovieList;
    }
}
