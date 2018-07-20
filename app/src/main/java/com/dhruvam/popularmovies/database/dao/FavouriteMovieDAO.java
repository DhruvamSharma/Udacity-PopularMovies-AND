package com.dhruvam.popularmovies.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.database.entity.MovieEntity;
import com.dhruvam.popularmovies.pojo.MovieResponse;

import java.util.List;
@Dao
public interface FavouriteMovieDAO {

    @Query("SELECT * from favourite_movies")
    LiveData<List<FavouriteMovies>> getFavouriteMovieList();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(FavouriteMovies movie);

    @Delete
    void deleteMovie(FavouriteMovies movie);

}
