package com.androideradev.www.moviespots.popular;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.androideradev.www.moviespots.Movie;
import com.androideradev.www.moviespots.databinding.PopularMoviesItemBinding;

class PopularMoviesAdapter extends ListAdapter<Movie, PopularMoviesAdapter.PopularMoviesViewHolder> {

    protected PopularMoviesAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PopularMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularMoviesViewHolder(
                PopularMoviesItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMoviesViewHolder holder, int position) {

    }

    public static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                    return false;
                }
            };

    public static class PopularMoviesViewHolder extends RecyclerView.ViewHolder {
        private PopularMoviesItemBinding mBinding;

        public PopularMoviesViewHolder(PopularMoviesItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
