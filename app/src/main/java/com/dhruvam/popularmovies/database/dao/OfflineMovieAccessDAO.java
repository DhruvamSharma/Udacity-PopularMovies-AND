package com.dhruvam.popularmovies.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dhruvam.popularmovies.database.entity.MovieEntity;
import com.dhruvam.popularmovies.pojo.MovieResponse;

import java.util.List;

@Dao
public interface OfflineMovieAccessDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addAllMovies(List<MovieEntity> resultList);

    @Query("SELECT * FROM offline_popular_movies")
    public List<MovieEntity> getAllMovies();

}
