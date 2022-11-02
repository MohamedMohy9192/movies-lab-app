package com.androideradev.www.moviespots;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

/**
 * @param <ResultType>  Type for the Cached data (database cached).
 * @param <RequestType> Type for the API response.
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final AppExecutors mAppExecutors;
    // results represent the data that are going to serve the UI and are going to be observed from UI
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    protected NetworkBoundResource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
        // Send loading state to UI
        result.setValue(Resource.loading(null));
        // Retrieve locale database cache
        final LiveData<ResultType> dbSource = loadFromDb();
        // Observe LiveData source of locale database cache
        result.addSource(dbSource, data -> {
            // decide either to fetch new data from network
            // or the data in locale cache Source is up to date
            // until decided remove the locale cache source
            // this will make any view observe the results live data
            // to stop listening
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                // the locale cache source is up to date
                // re-add it as a source to result and set its value
                // as success state with the data
                result.addSource(dbSource, newData -> {
                    setValue(Resource.success(newData));
                });
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    /**
     * Fetch the data from network and persist into DB and then
     * send it back to UI.
     */
    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource, newData -> {
            setValue(Resource.loading(newData));
        });

        result.addSource(apiResponse, response -> {
            // remove all sources to stop result live data observers from listening
            result.removeSource(apiResponse);
            result.removeSource(dbSource);

            if (response instanceof ApiResponse.ApiSuccessResponse) {
                mAppExecutors.diskIO().execute(() -> {
                    // in case of successful api response save the data to the local database cache
                    saveCallResult(processResponse(
                            (ApiResponse.ApiSuccessResponse<RequestType>) response));

                    mAppExecutors.mainThread().execute(() -> {
                        // we specially request a new live data returned by loadFromDb(),
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        result.addSource(loadFromDb(), newData -> {
                            setValue(Resource.success(newData));
                        });
                    });
                });
            } else if (response instanceof ApiResponse.ApiEmptyResponse) {
                // reload from disk whatever we had
                mAppExecutors.mainThread().execute(() -> {
                    result.addSource(loadFromDb(), newData -> {
                        setValue(Resource.success(newData));
                    });
                });

            } else if (response instanceof ApiResponse.ApiErrorResponse) {
                onFetchFailed();
                result.addSource(dbSource, newData -> {
                    setValue(Resource.error(
                            ((ApiResponse.ApiErrorResponse<RequestType>) response).getErrorMessage(),
                            newData));
                });
            }
        });


    }

    @WorkerThread
    protected RequestType processResponse(ApiResponse.ApiSuccessResponse<RequestType> response) {
        return response.getBody();
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    // Called when the fetch fails. the child class may want to reset components
    // like rate limiter
    @MainThread
    protected void onFetchFailed() {
    }

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}
