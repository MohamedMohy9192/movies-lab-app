package com.androideradev.www.moviespots.popular;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.androideradev.www.moviespots.AppExecutors;
import com.androideradev.www.moviespots.ConnectivityStatus;
import com.androideradev.www.moviespots.MovieMapper;
import com.androideradev.www.moviespots.MovieRepository;
import com.androideradev.www.moviespots.R;
import com.androideradev.www.moviespots.ServiceLocator;
import com.androideradev.www.moviespots.ViewModelFactory;
import com.androideradev.www.moviespots.data.DatabaseMovie;
import com.androideradev.www.moviespots.data.MovieLocaleDataSourceImpl;
import com.androideradev.www.moviespots.data.MoviesDatabase;
import com.androideradev.www.moviespots.databinding.FragmentPopularMoviesBinding;
import com.androideradev.www.moviespots.network.MovieRemoteDataSourceImpl;
import com.androideradev.www.moviespots.network.MoviesApiService;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class PopularMoviesFragment extends Fragment {

    private static final String TAG = "PopularMoviesFragment";

    private FragmentPopularMoviesBinding mBinding;

    private PopularMoviesAdapter mPopularMoviesAdapter;

    private boolean isConnected;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_popular_movies, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ViewModelFactory viewModelFactory = new ViewModelFactory(ServiceLocator.provideMovieRepository(requireContext()));

        final PopularMoviesViewModel popularMoviesViewModel =
                new ViewModelProvider(this, viewModelFactory).get(PopularMoviesViewModel.class);


        ConnectivityStatus.getConnectivityStatus(requireContext()).getNetworkStatus().observe(getViewLifecycleOwner(), networkStatus -> {
            switch (networkStatus) {
                case AVAILABLE:
                    isConnected = true;
                    Snackbar.make(requireView(), networkStatus.getMessage(), Snackbar.LENGTH_LONG).show();
                    break;
                case LOST:
                    isConnected = false;
                    Snackbar.make(requireView(), networkStatus.getMessage(), Snackbar.LENGTH_LONG).show();
                    break;
            }
        });

        RecyclerView recyclerView = mBinding.moviesRecyclerView;
        mPopularMoviesAdapter = new PopularMoviesAdapter();
        recyclerView.setAdapter(mPopularMoviesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        mBinding.moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastPosition = 0;
                if (layoutManager != null) {
                    lastPosition = layoutManager.findLastVisibleItemPosition();
                }
                if (lastPosition == mPopularMoviesAdapter.getItemCount() - 1) {
                    popularMoviesViewModel.loadNextPage();
                }
            }
        });

        popularMoviesViewModel.getResults().observe(getViewLifecycleOwner(), listResource -> {
            Log.d(TAG, "getResults: " + listResource);
            Log.d(TAG, "getResults:  get notified");
            if (listResource != null) {
                Log.d(TAG, "Status: " + listResource.status);
                Log.d(TAG, "Message: " + listResource.message);

                if (listResource.data != null) {
                    mPopularMoviesAdapter.submitList(MovieMapper.toMovies(listResource.data));
                    for (DatabaseMovie movie : listResource.data) {
                        Log.d(TAG, "Data: " + movie.getTitle());
                    }
                }
            } else {
                mPopularMoviesAdapter.submitList(null);
            }
        });

        popularMoviesViewModel.getLoadMoreStateLiveData().observe(getViewLifecycleOwner(), loadMoreState -> {
            if (loadMoreState == null) {

            } else {

                Log.d(TAG, "getLoadMoreStateLiveData: " + loadMoreState.isRunning());
                String error = loadMoreState.getErrorMessageIfNotHandled();
                if (error != null) {
                    Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show();
                }
            }
        });


        mBinding.button.setOnClickListener(v -> {
            Log.d(TAG, "Button Clicked");
            String q = mBinding.searchMovieEditText.getText().toString();
            dismissKeyboard(v.getWindowToken());

            popularMoviesViewModel.setQuery(q, isConnected);
        });
    }

    private void dismissKeyboard(IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }
}