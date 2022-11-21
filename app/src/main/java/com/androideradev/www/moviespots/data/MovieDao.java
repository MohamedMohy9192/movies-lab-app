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

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' AND release_date LIKE '%' || :year || '%'")
    LiveData<List<DatabaseMovie>> searchMoviesWithYear(String query, String year);

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    LiveData<List<DatabaseMovie>> searchMovies(String query);

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<DatabaseMovie> getMovie(int id);

    @Query("SELECT * FROM movies")
    LiveData<List<DatabaseMovie>> getAllMovies();
}
