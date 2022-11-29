package com.androideradev.www.moviespots.data;

import android.util.Log;
import android.util.SparseIntArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.androideradev.www.moviespots.AbsentLiveData;
import com.androideradev.www.moviespots.MovieMapper;
import com.androideradev.www.moviespots.MovieSearchResult;
import com.androideradev.www.moviespots.network.NetworkMovie;
import com.androideradev.www.moviespots.network.NetworkMovieContainer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import kotlin.jvm.functions.Function1;

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
    public void saveSearchMovieResult(String searchQuery, NetworkMovieContainer item) {
        List<Integer> movieIds = item.getMovies().stream().map(NetworkMovie::getId).collect(Collectors.toList());
        MovieSearchResult movieSearchResult = new MovieSearchResult(searchQuery, movieIds,
                item.getNextPage(), item.getTotalPages(), item.getTotalResults());

        List<DatabaseMovie> databaseMovies = MovieMapper.toDatabaseMovies(item.getMovies());

        mMoviesDatabase.runInTransaction(() -> {
            saveMovies(databaseMovies);
            mMovieDao.insertSearchMovieResult(movieSearchResult);
        });
    }

    @Override
    public MovieSearchResult findSearchResult(String query) {
        return mMovieDao.findSearchResult(query);
    }

    @Override
    public void saveNextPageSearchMovie(MovieSearchResult merged, List<NetworkMovie> movies) {
        List<DatabaseMovie> databaseMovies = MovieMapper.toDatabaseMovies(movies);
        mMoviesDatabase.runInTransaction(() -> {
            mMovieDao.insertSearchMovieResult(merged);
            mMovieDao.insertMovies(databaseMovies);
        });
    }

    private LiveData<List<DatabaseMovie>> order(List<Integer> movieIds) {
        SparseIntArray order = new SparseIntArray();

        for (int i = 0; i < movieIds.size(); i++) {
            order.put(movieIds.get(i), i);
        }

        return Transformations.map(mMovieDao.loadMoviesById(movieIds), movies -> {
            return movies.stream().sorted(Comparator.comparingInt(value -> {
                return order.get(value.getId());
            })).collect(Collectors.toList());
        });
    }
}
