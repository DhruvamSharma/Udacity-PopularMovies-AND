package com.dhruvam.popularmovies.view_model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;

import java.util.List;

public class FavouriteMoviesViewModel extends ViewModel {

    /**
     *
     //Do not store any activity context objects because View Model will outlive any Activity,
     //Thus the context object will live in the heap even when the screen is rotated and
     //While screen is rotated, a new context object will be created and thus creating leaks
     */

    //This is an application context object. This can be kept inside a view model. Because this will not outlive the application.
    private Context context;


    //LiveData reference that holds the data freshly retrieved from the database
    //This data will be provided to the UI Controller once there is a change in the database
    //Everytime a viewmodel is rotated, a new
    private LiveData<List<FavouriteMovies>> favouriteMovieList;


    //Empty constructor because the class is extending view model.
    //We could also make the class extend the AndroidViewModel and it would recieve an application context easily.
    //this way we could avoid keeping a refrence to the
    public FavouriteMoviesViewModel() {
        Log.e("in favourites", "fetching from database");

    }

    //Initialisation method for the view model. First time storing the data from the database
    public void init(Context mContext) {
        favouriteMovieList = OfflineMovieAccessDatabase.getInstance(mContext).getDao().getFavouriteMovieList();
    }


    //getters and setters for the livedata.
    public LiveData<List<FavouriteMovies>> getFavouriteMovieList() {
        return favouriteMovieList;
    }

    public void setFavouriteMovieList(LiveData<List<FavouriteMovies>> favouriteMovies) {
        this.favouriteMovieList = favouriteMovies;
    }
}
