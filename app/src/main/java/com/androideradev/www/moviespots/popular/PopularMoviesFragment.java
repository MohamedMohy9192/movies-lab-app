package com.androideradev.www.moviespots.popular;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androideradev.www.moviespots.R;
import com.androideradev.www.moviespots.databinding.FragmentPopularMoviesBinding;

public class PopularMoviesFragment extends Fragment {

    private FragmentPopularMoviesBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_popular_movies, container, false);
        return mBinding.getRoot();
    }
}