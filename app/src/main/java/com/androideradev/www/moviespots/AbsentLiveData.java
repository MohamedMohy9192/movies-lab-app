package com.androideradev.www.moviespots;

import androidx.lifecycle.LiveData;

public class AbsentLiveData<T> extends LiveData<T> {

    private AbsentLiveData() {
        postValue(null);
    }

    public static <T> LiveData<T> create() {
        return new AbsentLiveData<>();
    }
}
