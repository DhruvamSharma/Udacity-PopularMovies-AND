package com.dhruvam.popularmovies.database.database_instance;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.dhruvam.popularmovies.database.entity.FavouriteMovieEntity;

public abstract class FavouriteMoviesDatabase extends RoomDatabase {

    private static FavouriteMoviesDatabase sDatabase;
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favourite_movies_db";

    public static FavouriteMoviesDatabase getDatabase(Context context) {
        if ( sDatabase == null) {

            synchronized (LOCK) {
                sDatabase = Room.databaseBuilder(context.getApplicationContext(), FavouriteMoviesDatabase.class, FavouriteMoviesDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }

        }
        return sDatabase;
    }


}
