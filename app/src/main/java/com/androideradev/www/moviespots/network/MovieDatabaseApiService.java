package com.androideradev.www.moviespots.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDatabaseApiService {

    private static volatile MovieDatabaseApi sMovieDatabaseApiInstance = null;

    private MovieDatabaseApiService() {
        if (sMovieDatabaseApiInstance != null) {
            throw new RuntimeException("Use getMovieDatabaseApiInstance() method instead to create");
        }
    }

    public static MovieDatabaseApi getMovieDatabaseApiInstance() {
        if (sMovieDatabaseApiInstance == null) {
            synchronized (MovieDatabaseApiService.class) {
                if (sMovieDatabaseApiInstance == null) {
                    sMovieDatabaseApiInstance = getRetrofit().create(MovieDatabaseApi.class);
                }
            }
        }
        return sMovieDatabaseApiInstance;
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(NetworkUtilities.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

}
