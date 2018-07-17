package com.dhruvam.popularmovies.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dhruvam.popularmovies.database.entity.FavouriteMovieEntity;
import com.dhruvam.popularmovies.database.entity.MovieResponseEntity;

import java.util.List;
@Dao
public interface FavouriteMovieDAO {

    @Query("SELECT * from favourite_movies")
    List<MovieResponseEntity.Result> getFavouriteMovieList();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(MovieResponseEntity.Result movie);

}
