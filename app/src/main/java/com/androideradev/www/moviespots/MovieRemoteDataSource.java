package com.androideradev.www.moviespots;

import androidx.lifecycle.LiveData;

import com.androideradev.www.moviespots.network.NetworkMovieContainer;

import retrofit2.Call;

public interface MovieRemoteDataSource {
    LiveData<ApiResponse<NetworkMovieContainer>> searchMoviesApi(String query);

    Call<NetworkMovieContainer> searchNextPageMovie(String query, int nextPage);
}
