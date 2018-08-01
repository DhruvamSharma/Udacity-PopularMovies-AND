package com.dhruvam.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.dhruvam.popularmovies.database.dao.FavouriteMovieDAO;
import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.network.NetworkUtils;

import java.util.List;

/**
 * A utility class to fetch the data from various sources, be it from network, be it from database.
 */
public class FavouriteMoviesRepository {

    //Database reference for various data collection
    private static OfflineMovieAccessDatabase databaseService;


    //Repository initialization
    public void init(Context context) {
        databaseService = OfflineMovieAccessDatabase.getInstance(context);
    }



    public LiveData<List<FavouriteMovies>> getFavouriteMovieList() {

        return databaseService.getDao().getFavouriteMovieList();

    }

    public LiveData<FavouriteMovies> getFavouriteMovieById(int id) {
        return databaseService.getDao().getMovieByIdLiveData(id);
    }


}
