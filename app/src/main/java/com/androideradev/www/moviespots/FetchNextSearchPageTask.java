package com.androideradev.www.moviespots;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androideradev.www.moviespots.data.DatabaseMovie;
import com.androideradev.www.moviespots.data.MovieLocaleDataSource;
import com.androideradev.www.moviespots.network.NetworkMovie;
import com.androideradev.www.moviespots.network.NetworkMovieContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Response;

public class FetchNextSearchPageTask implements Runnable {
    private static final String TAG = "FetchNextSearchPageTask";
    private final String mSearchQuery;
    private final MovieLocaleDataSource mLocaleDataSource;
    private final MovieRemoteDataSource mRemoteDataSource;

    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();

    public LiveData<Resource<Boolean>> getLiveData() {
        return liveData;
    }

    public FetchNextSearchPageTask(String searchQuery, MovieLocaleDataSource localeDataSource, MovieRemoteDataSource remoteDataSource) {
        mSearchQuery = searchQuery;
        mLocaleDataSource = localeDataSource;
        mRemoteDataSource = remoteDataSource;
    }


    @Override
    public void run() {
        MovieSearchResult current = mLocaleDataSource.findSearchResult(mSearchQuery);
        if (current == null) {
            liveData.postValue(null);
            return;
        }
        Integer nextPage = current.getNextPageNumber();
        if (nextPage == null) {
            liveData.postValue(Resource.success(false));
            return;
        }
        Resource<Boolean> newValue = null;
        try {
            Response<NetworkMovieContainer> response = mRemoteDataSource.searchNextPageMovie(mSearchQuery, nextPage).execute();
            ApiResponse<NetworkMovieContainer> apiResponse = ApiResponse.create(response);
            if (apiResponse instanceof ApiResponse.ApiSuccessResponse) {

                // we merge all repo ids into 1 list so that it is easier to fetch the
                // result list.
                List<Integer> ids = new ArrayList<>();
                ids.addAll(current.getMovieIds());

                ids.addAll(
                        ((ApiResponse.ApiSuccessResponse<NetworkMovieContainer>) apiResponse)
                                .getBody()
                                .getMovies()
                                .stream()
                                .map(NetworkMovie::getId)
                                .collect(Collectors.toList())
                );
                MovieSearchResult merged = new MovieSearchResult(
                        mSearchQuery,
                        ids,
                        ((ApiResponse.ApiSuccessResponse<NetworkMovieContainer>) apiResponse).getBody().getNextPage(),
                        ((ApiResponse.ApiSuccessResponse<NetworkMovieContainer>) apiResponse).getBody().getTotalPages(),
                        ((ApiResponse.ApiSuccessResponse<NetworkMovieContainer>) apiResponse).getBody().getTotalResults()
                );

                List<DatabaseMovie> databaseMovies = MovieMapper.toDatabaseMovies(((ApiResponse.ApiSuccessResponse<NetworkMovieContainer>) apiResponse).getBody().getMovies());
                mLocaleDataSource.saveSearchResult(merged, databaseMovies);

                newValue = Resource.success(((ApiResponse.ApiSuccessResponse<NetworkMovieContainer>) apiResponse).getBody().getNextPage() != null);
            } else if (apiResponse instanceof ApiResponse.ApiEmptyResponse) {
                newValue = Resource.success(false);
            } else if (apiResponse instanceof ApiResponse.ApiErrorResponse) {
                newValue = Resource.error(((ApiResponse.ApiErrorResponse<NetworkMovieContainer>) apiResponse).getErrorMessage(), true);
            }
        } catch (IOException e) {
            newValue = Resource.error(e.getMessage() != null ? e.getMessage() : "Unknown Error", true);
        }
        liveData.postValue(newValue);
    }
}
