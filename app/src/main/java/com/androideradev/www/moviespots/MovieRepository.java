package com.androideradev.www.moviespots;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.androideradev.www.moviespots.data.DatabaseMovie;
import com.androideradev.www.moviespots.data.MovieLocaleDataSource;
import com.androideradev.www.moviespots.network.NetworkMovie;
import com.androideradev.www.moviespots.network.NetworkMovieContainer;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    private static final String TAG = "MovieRepository";

    private final MovieLocaleDataSource mLocaleDataSource;
    private final MovieRemoteDataSource mRemoteDataSource;
    private final AppExecutors mAppExecutors;
    // https://api.themoviedb.org/3/movie/663712?api_key=92962d4d79dd32ef1f38549ab7a2ab2f&language=en-US&append_to_response=images,credits,reviews,videos&include_image_language=en

    private static volatile MovieRepository sMovieRepository;

    private MovieRepository(MovieLocaleDataSource localeDataSource, MovieRemoteDataSource remoteDataSource, AppExecutors appExecutors) {
        mLocaleDataSource = localeDataSource;
        mRemoteDataSource = remoteDataSource;
        mAppExecutors = appExecutors;
    }

    public static MovieRepository getInstance(MovieLocaleDataSource localeDataSource, MovieRemoteDataSource remoteDataSource, AppExecutors appExecutors) {
        if (sMovieRepository == null) {
            synchronized (MovieRepository.class) {
                if (sMovieRepository == null) {
                    sMovieRepository = new MovieRepository(localeDataSource, remoteDataSource, appExecutors);
                }
            }
        }
        return sMovieRepository;
    }


    public LiveData<Resource<Boolean>> searchNextPage(String query) {
        FetchNextSearchPageTask fetchNextSearchPageTask = new FetchNextSearchPageTask(
                query,
                mLocaleDataSource,
                mRemoteDataSource
        );
        mAppExecutors.networkIO().execute(fetchNextSearchPageTask);
        return fetchNextSearchPageTask.getLiveData();
    }

    public LiveData<Resource<List<DatabaseMovie>>> searchMovies(String query) {
        return new NetworkBoundResource<List<DatabaseMovie>, NetworkMovieContainer>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull NetworkMovieContainer item) {
                Log.d(TAG, "NetworkBoundResource: " + item.getMovies().size());
                if (item.getMovies() != null) {
                    Log.d(TAG, "Next Page: " + item.getNextPage());
                    mLocaleDataSource.saveSearchMovieResult(query, item);
                    for (NetworkMovie movie : item.getMovies()) {
                        Log.d("FetchNextSearchPageTask", "saveCallResult: " + movie.getId());
                    }
                    //mLocaleDataSource.saveMovies(MovieMapper.toDatabaseMovies(item.getMovies()));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<DatabaseMovie> data) {
                Log.d("FACE", "shouldFetch: " + (data == null || data.isEmpty()));
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<DatabaseMovie>> loadFromDb() {
                Log.d(TAG, "loadFromDb");
                LiveData<List<DatabaseMovie>> listLiveData = mLocaleDataSource.searchMoviesDatabase(query);
                listLiveData.observeForever(movies -> {
                    Log.d("FetchNextSearchPageTask", "loadFromDb: get notified");
                    Log.d("FACE", "loadFromDb: " + String.valueOf(movies));
                    if (movies != null) {
                        for (DatabaseMovie movie : movies) {
                            //  Log.d("FetchNextSearchPageTask", "loadFromDb: " + movie.getId());
                        }
                    }
                });
                return listLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NetworkMovieContainer>> createCall() {
                Log.d(TAG, "createCall");
                return mRemoteDataSource.searchMoviesApi(query);
            }
        }.getAsLiveData();
    }

    /*public LiveData<Resource<List<Movie>>> search(String query) {
        LiveData<Resource<List<DatabaseMovie>>> resourceLiveData = searchMovies(query);
        return Transformations.map(resourceLiveData, resourceDatabaseMovies -> {
            if (resourceDatabaseMovies.data != null) {

                return new Resource<>(resourceDatabaseMovies.status,
                        MovieMapper.toMovies(resourceDatabaseMovies.data),
                        resourceDatabaseMovies.message);
            }

            return new Resource<>(resourceDatabaseMovies.status, null, resourceDatabaseMovies.message);
        });
    }*/
}
