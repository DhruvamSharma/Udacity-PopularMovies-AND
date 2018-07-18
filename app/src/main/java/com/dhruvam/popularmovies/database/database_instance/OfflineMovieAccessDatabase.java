package com.dhruvam.popularmovies.database.database_instance;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.dhruvam.popularmovies.database.dao.FavouriteMovieDAO;
import com.dhruvam.popularmovies.database.dao.OfflineMovieAccessDAO;
import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.database.entity.MovieEntity;

@Database( entities = {FavouriteMovies.class, MovieEntity.class} , version = 1 , exportSchema = false)
public abstract class OfflineMovieAccessDatabase extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popular_movies_db";
    private static OfflineMovieAccessDatabase sDatabase;

    private OfflineMovieAccessDatabase() {

    }

    public static OfflineMovieAccessDatabase getInstance(Context context) {
        if (sDatabase == null) {
            sDatabase = Room.databaseBuilder(context, OfflineMovieAccessDatabase.class, DATABASE_NAME).build();
        }
        return sDatabase;
    }

    public abstract OfflineMovieAccessDAO getMovieDao();

    public abstract FavouriteMovieDAO getDao();

}
