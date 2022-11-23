package com.androideradev.www.moviespots.popular;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.androideradev.www.moviespots.AbsentLiveData;
import com.androideradev.www.moviespots.MovieRepository;
import com.androideradev.www.moviespots.Resource;
import com.androideradev.www.moviespots.data.DatabaseMovie;

import java.util.List;

public class PopularMoviesViewModel extends ViewModel {

    private static final String TAG = "PopularMoviesViewModel";

    private MediatorLiveData<Resource<List<DatabaseMovie>>> mResults = new MediatorLiveData<>();

    private final MovieRepository mMovieRepository;

    public PopularMoviesViewModel(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;

    }

    public LiveData<Resource<List<DatabaseMovie>>> getResults() {
        return mResults;
    }

    public void searchMoviesApi(String query) {

        final LiveData<Resource<List<DatabaseMovie>>> source = mMovieRepository.searchMovies(query);
        mResults.addSource(source, listResource -> {
            mResults.setValue(listResource);
        });

    }


}
