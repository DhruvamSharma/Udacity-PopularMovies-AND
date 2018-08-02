package com.dhruvam.popularmovies.view_model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.repository.FavouriteMoviesRepository;

import java.util.List;

/**
 *
 //Do not store any activity context objects because View Model will outlive any Activity,
 //Thus the context object will live in the heap even when the screen is rotated and
 //While screen is rotated, a new context object will be created and thus creating leaks
 */
public class FavouriteMoviesViewModel extends ViewModel {

    //Repository reference to fetch the data
    private FavouriteMoviesRepository favouriteMoviesRepository;


    //LiveData reference that holds the data freshly retrieved from the database
    //This data will be provided to the UI Controller once there is a change in the database
    //Everytime a viewmodel is rotated, a new
    private LiveData<List<FavouriteMovies>> favouriteMovieList;


    //Empty constructor because the class is extending view model.
    //We could also make the class extend the AndroidViewModel and it would receive an application context easily.
    //this way we could avoid keeping a refrence to the context
    public FavouriteMoviesViewModel() {

    }

    //Initialisation method for the view model and the repository. First time storing the data from the repository
    public void init(Context mContext) {

        if(favouriteMoviesRepository == null) {

            Log.e("in favourites", "fetching from database");

            favouriteMoviesRepository = new FavouriteMoviesRepository();
            favouriteMoviesRepository.init(mContext);
            favouriteMovieList = favouriteMoviesRepository.getFavouriteMovieList();
        }


    }


    //public getters and setters for the livedata.
    public LiveData<List<FavouriteMovies>> getFavouriteMovieList() {
        return favouriteMovieList;
    }

    public void setFavouriteMovieList(LiveData<List<FavouriteMovies>> favouriteMovies) {
        this.favouriteMovieList = favouriteMovies;
    }
}
