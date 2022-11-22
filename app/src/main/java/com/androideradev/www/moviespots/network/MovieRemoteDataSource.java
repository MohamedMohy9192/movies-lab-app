package com.androideradev.www.moviespots.network;

import androidx.lifecycle.LiveData;

import com.androideradev.www.moviespots.ApiResponse;
import com.androideradev.www.moviespots.DataSource;
import com.androideradev.www.moviespots.data.DatabaseMovie;


import java.util.List;

public class MovieRemoteDataSource implements DataSource {

    private static volatile MovieRemoteDataSource sMovieRemoteDataSource;

    private final MoviesApi mMoviesApi;

    public static final String MOVIES_API_KEY = NetworkUtilities.API_KEY;

    private MovieRemoteDataSource(MoviesApi moviesApi) {
        mMoviesApi = moviesApi;
    }

    public static MovieRemoteDataSource getInstance(MoviesApi moviesApi) {
        if (sMovieRemoteDataSource == null) {
            synchronized (MovieRemoteDataSource.class) {
                if (sMovieRemoteDataSource == null) {
                    sMovieRemoteDataSource = new MovieRemoteDataSource(moviesApi);
                }
            }
        }

        return sMovieRemoteDataSource;
    }

    @Override
    public LiveData<ApiResponse<NetworkMovieContainer>> searchMoviesApi(String query) {
        return mMoviesApi.searchMovie(MOVIES_API_KEY, query, 1, "en-US");
    }

    @Override
    public LiveData<List<DatabaseMovie>> searchMoviesDatabase(String query) {
        return null;
    }

    @Override
    public void saveMovies(List<DatabaseMovie> movies) {

    }
}
