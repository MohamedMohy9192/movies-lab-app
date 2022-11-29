package com.androideradev.www.moviespots.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.androideradev.www.moviespots.MovieSearchResult;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSearchMovieResult(MovieSearchResult result);

    @Query("SELECT * FROM movie_search_result WHERE query_text = :query")
    LiveData<MovieSearchResult> search(String query);

    @Query("SELECT * FROM movies WHERE id in (:repoIds)")
    LiveData<List<DatabaseMovie>> loadMoviesById(List<Integer> repoIds);

    @Query("SELECT * FROM movie_search_result WHERE query_text = :query")
    MovieSearchResult findSearchResult(String query);
}
