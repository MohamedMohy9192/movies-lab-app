package com.androideradev.www.moviespots.data;

import androidx.lifecycle.LiveData;

import com.androideradev.www.moviespots.MovieSearchResult;

import java.util.List;

public interface MovieLocaleDataSource {

    LiveData<List<DatabaseMovie>> searchMoviesDatabase(String query);

    void saveMovies(List<DatabaseMovie> movies);

    void saveSearchResult(MovieSearchResult movieSearchResult, List<DatabaseMovie> databaseMovies);

    MovieSearchResult findSearchResult(String query);



    void saveSearchMovieResult(MovieSearchResult movieSearchResult);
}
