package com.androideradev.www.moviespots.popular;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.androideradev.www.moviespots.AbsentLiveData;
import com.androideradev.www.moviespots.Movie;
import com.androideradev.www.moviespots.MovieMapper;
import com.androideradev.www.moviespots.MovieRepository;
import com.androideradev.www.moviespots.Resource;
import com.androideradev.www.moviespots.data.DatabaseMovie;

import java.util.List;
import java.util.Locale;

public class PopularMoviesViewModel extends ViewModel {

    private static final String TAG = "PopularMoviesViewModel";

    private final MutableLiveData<String> query = new MutableLiveData<>();

    private final LiveData<Resource<List<Movie>>> mResults;

    private final MovieRepository mMovieRepository;

    private final NextPageHandler mNextPageHandler;

    private final LiveData<LoadMoreState> mLoadMoreStateLiveData;

    public PopularMoviesViewModel(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
        mNextPageHandler = new NextPageHandler(mMovieRepository);

        mLoadMoreStateLiveData = mNextPageHandler.getLoadMoreState();

        mResults = Transformations.switchMap(query, search -> {
            if (search.isBlank()) {
                Log.d(TAG, "PopularMoviesViewModel: " + search);
                return AbsentLiveData.create();
            } else {
                Log.d(TAG, "PopularMoviesViewModel: get notified ");
                return mMovieRepository.search(search);
            }
        });

        mResults.observeForever(listResource -> {
            Log.d("TAG", "PopularMoviesViewModel: get notified");
        });
    }

    public LiveData<Resource<List<Movie>>> getResults() {
        return mResults;
    }

    public LiveData<LoadMoreState> getLoadMoreStateLiveData() {
        return mLoadMoreStateLiveData;
    }

    public LiveData<String> getQuery() {
        return query;
    }

    public void setQuery(String originalInput) {
        String input = originalInput.toLowerCase(Locale.getDefault()).trim();

        if (input.equals(query.getValue())) {
            return;
        }
        mNextPageHandler.reset();
        query.setValue(input);

    }

    public void loadNextPage() {
        String searchQuery = query.getValue();
        if (searchQuery != null) {
            if (!searchQuery.isEmpty()) {
                mNextPageHandler.queryNextPage(searchQuery);
            }
        }
    }

    public void refresh() {
        if (query.getValue() != null) {
            query.setValue(query.getValue());
        }
    }

    static class LoadMoreState {
        private final boolean isRunning;
        @Nullable
        private final String errorMessage;

        private boolean handledError = false;

        public LoadMoreState(boolean isRunning, @Nullable String errorMessage) {
            this.isRunning = isRunning;
            this.errorMessage = errorMessage;
        }

        public String getErrorMessageIfNotHandled() {
            if (handledError) {
                return null;
            }
            handledError = true;
            return errorMessage;
        }

        public boolean isRunning() {
            return isRunning;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean isHandledError() {
            return handledError;
        }

        public void setHandledError(boolean handledError) {
            this.handledError = handledError;
        }
    }

    static class NextPageHandler implements Observer<Resource<Boolean>> {
        @Nullable
        private LiveData<Resource<Boolean>> nextPageLiveData = null;

        private final MutableLiveData<LoadMoreState> loadMoreState = new MutableLiveData<>();
        @Nullable
        private String query = null;
        private boolean hasMore = false;

        private final MovieRepository mMovieRepository;

        NextPageHandler(MovieRepository movieRepository) {
            mMovieRepository = movieRepository;
            reset();
        }

        public LiveData<LoadMoreState> getLoadMoreState() {
            return loadMoreState;
        }

        void queryNextPage(String query) {
            Log.d(TAG, "queryNextPage: " + query);
            Log.d(TAG, "queryNextPage: " + (this.query != null && this.query.equals(query)));
            if (this.query != null && this.query.equals(query)) {
                Log.d(TAG, "queryNextPage: Return");
                return;
            }
            Log.d(TAG, "queryNextPage: Continuation");
            unregister();
            this.query = query;
            nextPageLiveData = mMovieRepository.searchNextPage(query);
            loadMoreState.setValue(new LoadMoreState(true, null));
            if (nextPageLiveData != null) {
                nextPageLiveData.observeForever(this);
            }
        }

        @Override
        public void onChanged(Resource<Boolean> result) {
            if (result == null) {
                reset();
            } else {
                switch (result.status) {
                    case SUCCESS:
                        hasMore = Boolean.TRUE.equals(result.data);
                        unregister();
                        loadMoreState.setValue(
                                new LoadMoreState(false, null)
                        );

                        break;
                    case ERROR:
                        hasMore = true;
                        unregister();
                        loadMoreState.setValue(
                                new LoadMoreState(false, result.message)
                        );
                        break;
                    case LOADING:
                        // ignore
                        break;
                }
            }
        }

        private void unregister() {
            if (nextPageLiveData != null) {
                nextPageLiveData.removeObserver(this);
            }
            nextPageLiveData = null;
            if (hasMore) {
                query = null;
            }
        }

        private void reset() {
            unregister();
            hasMore = true;
            loadMoreState.setValue(new LoadMoreState(false, null));
        }
    }
}
