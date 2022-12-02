package com.androideradev.www.moviespots.popular;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androideradev.www.moviespots.ConnectivityStatus;
import com.androideradev.www.moviespots.R;
import com.androideradev.www.moviespots.ServiceLocator;
import com.androideradev.www.moviespots.ViewModelFactory;
import com.androideradev.www.moviespots.databinding.FragmentPopularMoviesBinding;
import com.google.android.material.snackbar.Snackbar;

public class PopularMoviesFragment extends Fragment {

    private static final String TAG = "PopularMoviesFragment";

    private FragmentPopularMoviesBinding mBinding;

    private PopularMoviesAdapter mPopularMoviesAdapter;
    private PopularMoviesViewModel mPopularMoviesViewModel;

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


        ViewModelFactory viewModelFactory =
                new ViewModelFactory(ServiceLocator.provideMovieRepository(requireContext()));

        mPopularMoviesViewModel =
                new ViewModelProvider(this, viewModelFactory).get(PopularMoviesViewModel.class);


        mBinding.setLifecycleOwner(getViewLifecycleOwner());

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
                    mPopularMoviesViewModel.loadNextPage();
                }
            }
        });

        mBinding.setQuery(mPopularMoviesViewModel.getQuery());
        mBinding.setSearchResult(mPopularMoviesViewModel.getResults());
        mPopularMoviesViewModel.getResults().observe(getViewLifecycleOwner(), listResource -> {
            Log.d(TAG, "getResults: " + listResource);
            Log.d(TAG, "getResults:  get notified");
            if (listResource != null) {
                Log.d(TAG, "Status: " + listResource.status);
                Log.d(TAG, "Message: " + listResource.message);


                mPopularMoviesAdapter.submitList(listResource.data);

            } else {
                mPopularMoviesAdapter.submitList(null);
            }
        });

        mPopularMoviesViewModel.getLoadMoreStateLiveData().observe(getViewLifecycleOwner(), loadMoreState -> {
            if (loadMoreState == null) {
                mBinding.setLoadingMore(false);
            } else {
                mBinding.setLoadingMore(loadMoreState.isRunning());
                Log.d(TAG, "getLoadMoreStateLiveData: " + loadMoreState.isRunning());
                String error = loadMoreState.getErrorMessageIfNotHandled();
                if (error != null) {
                    Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show();
                }
            }
        });
        initConnectivityStatus();
        initSearchInputListener();

        mBinding.setCallback(() -> {
            mPopularMoviesViewModel.refresh();
        });
    }

    private void initConnectivityStatus() {
        ConnectivityStatus.getConnectivityStatus(requireContext()).getNetworkStatus().observe(getViewLifecycleOwner(), networkStatus -> {
            switch (networkStatus) {
                case AVAILABLE:
                    mPopularMoviesViewModel.refresh();
                    Snackbar.make(requireView(), networkStatus.getMessage(), Snackbar.LENGTH_LONG).show();
                    break;
                case LOST:
                    Snackbar.make(requireView(), networkStatus.getMessage(), Snackbar.LENGTH_LONG).show();
                    break;


            }
        });
    }

    private void initSearchInputListener() {
        mBinding.input.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view);
                return true;
            } else {
                return false;
            }
        });
        mBinding.input.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view);
                return true;
            } else {
                return false;
            }
        });
    }

    private void doSearch(View view) {
        Log.d(TAG, "Button Clicked");
        String query = mBinding.input.getText().toString();
        // Dismiss keyboard
        dismissKeyboard(view.getWindowToken());
        mPopularMoviesViewModel.setQuery(query);
    }

    private void dismissKeyboard(IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }
}