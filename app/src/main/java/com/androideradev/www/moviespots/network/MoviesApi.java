package com.androideradev.www.moviespots.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    @GET("search/movie")
    Call<NetworkMovieContainer> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query);


    @GET("movie/{id}")
    Call<NetworkMovie> getMovieById(
            @Path("id") int id,
            @Query("api_key") String key,
            @Query("language") String language
    );
}

