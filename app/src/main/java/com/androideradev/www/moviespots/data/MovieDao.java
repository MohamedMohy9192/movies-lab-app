package com.androideradev.www.moviespots.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertMovies(List<DatabaseMovie> movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMovie(DatabaseMovie movie);

    @Query("SELECT * FROM movies WHERE title LIKE :query AND release_date = strftime('%Y', :year)")
    LiveData<DatabaseMovie> searchMovies(String query, String year);

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<DatabaseMovie> getMovie(int id);

    @Query("SELECT * FROM movies")
    LiveData<DatabaseMovie> getAllMovies();
}
