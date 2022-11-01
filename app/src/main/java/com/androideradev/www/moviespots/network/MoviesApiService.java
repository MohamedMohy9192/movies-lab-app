package com.androideradev.www.moviespots.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesApiService {

    private static volatile MoviesApiService sMoviesApiService = null;
    private final MoviesApi mMoviesApi;

    private MoviesApiService() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkUtilities.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .build())
                .build();

        mMoviesApi = retrofit.create(MoviesApi.class);
    }

    public static MoviesApiService getInstance() {
        if (sMoviesApiService == null) {
            synchronized (MoviesApiService.class) {
                if (sMoviesApiService == null) {
                    sMoviesApiService = new MoviesApiService();
                }
            }
        }
        return sMoviesApiService;
    }

    public MoviesApi getsMoviesApi() {
        return mMoviesApi;
    }
}
