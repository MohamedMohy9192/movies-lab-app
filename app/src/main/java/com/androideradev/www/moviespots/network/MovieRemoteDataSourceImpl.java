package com.androideradev.www.moviespots.network;

import androidx.lifecycle.LiveData;

import com.androideradev.www.moviespots.ApiResponse;
import com.androideradev.www.moviespots.MovieRemoteDataSource;
import com.androideradev.www.moviespots.data.DatabaseMovie;


import java.util.List;

import retrofit2.Call;

public class MovieRemoteDataSourceImpl implements MovieRemoteDataSource {

    private static volatile MovieRemoteDataSourceImpl sMovieRemoteDataSource;

    private final MoviesApi mMoviesApi;

    public static final String MOVIES_API_KEY = NetworkUtilities.API_KEY;

    private MovieRemoteDataSourceImpl(MoviesApi moviesApi) {
        mMoviesApi = moviesApi;
    }

    public static MovieRemoteDataSourceImpl getInstance(MoviesApi moviesApi) {
        if (sMovieRemoteDataSource == null) {
            synchronized (MovieRemoteDataSourceImpl.class) {
                if (sMovieRemoteDataSource == null) {
                    sMovieRemoteDataSource = new MovieRemoteDataSourceImpl(moviesApi);
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
    public Call<NetworkMovieContainer> searchNextPageMovie(String query, int nextPage) {
        return mMoviesApi.searchNextPageMovie(MOVIES_API_KEY, query, nextPage, "en-US");
    }

}
