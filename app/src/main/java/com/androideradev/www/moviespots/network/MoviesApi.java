package com.androideradev.www.moviespots.network;

import androidx.lifecycle.LiveData;

import com.androideradev.www.moviespots.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    @GET("search/movie")
    LiveData<ApiResponse<NetworkMovieContainer>> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page,
            @Query("language") String language/*,
            @Query("include_adult") boolean includeAdult,
            @Query("region") String region,
            @Query("primary_release_year") String year*/);


    @GET("movie/{id}")
    LiveData<ApiResponse<NetworkMovie>> getMovieById(
            @Path("id") int id,
            @Query("api_key") String key,
            @Query("language") String language
    );

    @GET("search/movie")
    Call<NetworkMovieContainer> searchNextPageMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page,
            @Query("language") String language/*,
            @Query("include_adult") boolean includeAdult,
            @Query("region") String region,
            @Query("primary_release_year") String year*/);
}

