package com.dhruvam.popularmovies.view_model;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.dhruvam.popularmovies.database.database_instance.OfflineMovieAccessDatabase;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.repository.FavouriteMoviesRepository;

/**
 *
 //Do not store any activity context objects because View Model will outlive any Activity,
 //Thus the context object will live in the heap even when the screen is rotated and
 //While screen is rotated, a new context object will be created and thus creating leaks
 */
public class FavouriteMovieViewModelById extends ViewModel {

    //LiveData reference that holds the data freshly retrieved from the database
    //This data will be provided to the UI Controller once there is a change in the database
    //Everytime a viewmodel is rotated, a new
    private LiveData<FavouriteMovies> favouriteMovie;

    //Movie id
    private int mId;

    //TODO Dagger2 to be used for dependncy injection
    //Repository reference to fetch the data
    private FavouriteMoviesRepository favouriteMoviesRepository;

    //Reposiory initialization method
    public void init() {

        favouriteMovie = favouriteMoviesRepository.getFavouriteMovieById(mId);

    }

    //A constructor that receives Database and movieId for which it wants to get the details for.
    public FavouriteMovieViewModelById( int movieId ) {
        mId = movieId;
        favouriteMoviesRepository = new FavouriteMoviesRepository();
    }

    //public getters and setters for the Favourite Movies
    public LiveData<FavouriteMovies> getFavouriteMovieById() {
        return favouriteMovie;
    }
}
