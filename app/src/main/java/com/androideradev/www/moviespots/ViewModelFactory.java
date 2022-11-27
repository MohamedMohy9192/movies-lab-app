package com.androideradev.www.moviespots;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.androideradev.www.moviespots.popular.PopularMoviesViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository mMovieRepository;

    public ViewModelFactory(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PopularMoviesViewModel.class)) {
            return (T) new PopularMoviesViewModel(mMovieRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
