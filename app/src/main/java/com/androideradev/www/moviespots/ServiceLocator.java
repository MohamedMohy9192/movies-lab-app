package com.androideradev.www.moviespots;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Room;

import com.androideradev.www.moviespots.data.MovieLocaleDataSource;
import com.androideradev.www.moviespots.data.MovieLocaleDataSourceImpl;
import com.androideradev.www.moviespots.data.MoviesDatabase;
import com.androideradev.www.moviespots.network.MovieRemoteDataSourceImpl;
import com.androideradev.www.moviespots.network.MoviesApiService;

public class ServiceLocator {

    private static MoviesDatabase sMoviesDatabase;
    private static MovieRepository sMovieRepository;
    private static MoviesApiService sMoviesApiService;

    public static MovieRepository provideMovieRepository(Context context) {
        if (sMovieRepository == null) {
            sMovieRepository = createMovieRepository(context);
        }
        return sMovieRepository;
    }

    private static MovieRepository createMovieRepository(Context context) {

        return MovieRepository.getInstance(
                // Remote DataSource
                createMovieLocalDataSource(context),
                createMovieRemoteDataSource(),
                createAppExecutors()
        );
    }

    private static MovieLocaleDataSource createMovieLocalDataSource(Context context) {
        if (sMoviesDatabase == null) {
            sMoviesDatabase = createDataBase(context, false);
        }
        return MovieLocaleDataSourceImpl.getInstance(sMoviesDatabase);
    }

    private static MovieRemoteDataSource createMovieRemoteDataSource() {
        MoviesApiService moviesApiService = MoviesApiService.getInstance();
        return MovieRemoteDataSourceImpl.getInstance(moviesApiService.getsMoviesApi());
    }

    private static AppExecutors createAppExecutors() {
        return AppExecutors.getInstance();
    }

    @VisibleForTesting
    private static MoviesDatabase createDataBase(Context context, boolean inMemory) {
        MoviesDatabase result;
        if (inMemory) {
            // Use a faster in-memory database for tests
            result = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), MoviesDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        } else {
            result = MoviesDatabase.getInstance(context);
        }
        sMoviesDatabase = result;
        return result;
    }
}
