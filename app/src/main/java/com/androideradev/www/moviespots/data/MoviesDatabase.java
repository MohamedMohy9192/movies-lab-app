package com.androideradev.www.moviespots.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.androideradev.www.moviespots.MovieSearchResult;

@TypeConverters({GenreIdConverter.class})
@Database(entities = {DatabaseMovie.class, MovieSearchResult.class}, version = 1, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static volatile MoviesDatabase sInstance;

    private static final String DATABASE_NAME = "movies-db";

    public abstract MovieDao moviesDao();

    public static MoviesDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MoviesDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MoviesDatabase.class,
                            DATABASE_NAME
                    ).build();
                }
            }
        }
        return sInstance;
    }
}
