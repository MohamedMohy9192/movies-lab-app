package com.androideradev.www.moviespots.data;

import androidx.lifecycle.LiveData;

import com.androideradev.www.moviespots.MovieSearchResult;
import com.androideradev.www.moviespots.network.NetworkMovie;
import com.androideradev.www.moviespots.network.NetworkMovieContainer;

import java.util.List;

public interface MovieLocaleDataSource {

    LiveData<List<DatabaseMovie>> searchMoviesDatabase(String query);

    void saveMovies(List<DatabaseMovie> movies);

    void saveSearchMovieResult(String searchQuery, NetworkMovieContainer item);

    MovieSearchResult findSearchResult(String query);

    void saveNextPageSearchMovie(MovieSearchResult merged, List<NetworkMovie> movies);
}
