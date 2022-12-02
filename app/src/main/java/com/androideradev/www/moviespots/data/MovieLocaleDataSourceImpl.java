package com.androideradev.www.moviespots.data;

import android.util.Log;
import android.util.SparseIntArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.androideradev.www.moviespots.AbsentLiveData;
import com.androideradev.www.moviespots.MovieSearchResult;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MovieLocaleDataSourceImpl implements MovieLocaleDataSource {

    private static volatile MovieLocaleDataSourceImpl sMovieLocaleDataSource;

    private final MoviesDatabase mMoviesDatabase;

    private final MovieDao mMovieDao;

    private MovieLocaleDataSourceImpl(MoviesDatabase moviesDatabase) {
        mMoviesDatabase = moviesDatabase;
        mMovieDao = moviesDatabase.moviesDao();
    }

    public static MovieLocaleDataSourceImpl getInstance(MoviesDatabase moviesDatabase) {
        if (sMovieLocaleDataSource == null) {
            synchronized (MovieLocaleDataSourceImpl.class) {
                if (sMovieLocaleDataSource == null) {
                    sMovieLocaleDataSource = new MovieLocaleDataSourceImpl(moviesDatabase);
                }
            }
        }
        return sMovieLocaleDataSource;
    }


    @Override
    public LiveData<List<DatabaseMovie>> searchMoviesDatabase(String query) {
        return Transformations.switchMap(mMovieDao.search(query), searchResult -> {
            if (searchResult == null) {
                AbsentLiveData.create().observeForever(o -> Log.d("FACE", "DataSource: " + o));
                return AbsentLiveData.create();
            } else {

                return order(searchResult.getMovieIds());
            }
        });
    }

    @Override
    public void saveMovies(List<DatabaseMovie> movies) {
        mMovieDao.insertMovies(movies);
    }

    @Override
    public void saveSearchResult(MovieSearchResult movieSearchResult, List<DatabaseMovie> databaseMovies) {

        mMoviesDatabase.runInTransaction(() -> {
            saveSearchMovieResult(movieSearchResult);
            saveMovies(databaseMovies);
        });
    }

    @Override
    public MovieSearchResult findSearchResult(String query) {
        return mMovieDao.findSearchResult(query);
    }


    @Override
    public void saveSearchMovieResult(MovieSearchResult movieSearchResult) {
        mMovieDao.insertSearchMovieResult(movieSearchResult);
    }

    private LiveData<List<DatabaseMovie>> order(List<Integer> movieIds) {
        SparseIntArray order = new SparseIntArray();

        for (int index = 0; index < movieIds.size(); index++) {
            order.put(movieIds.get(index), index);
        }

        return Transformations.map(mMovieDao.loadMoviesById(movieIds), movies -> {
            return movies.stream().sorted(Comparator.comparingInt(databaseMovie -> {
                return order.get(databaseMovie.getId());
            })).collect(Collectors.toList());
        });
    }
}
