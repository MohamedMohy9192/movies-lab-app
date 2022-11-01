package com.androideradev.www.moviespots.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NetworkMovieContainer {

    private int mPage;
    @SerializedName("results")
    private List<NetworkMovie> mMovies;
    @SerializedName("total_pages")
    private int mTotalPages;
    @SerializedName("total_results")
    private int mTotalResults;

    public NetworkMovieContainer(int page, List<NetworkMovie> movies, int totalPages, int totalResults) {
        mPage = page;
        mMovies = movies;
        mTotalPages = totalPages;
        mTotalResults = totalResults;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public List<NetworkMovie> getMovies() {
        return mMovies;
    }

    public void setMovies(List<NetworkMovie> movies) {
        mMovies = movies;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int totalPages) {
        mTotalPages = totalPages;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        mTotalResults = totalResults;
    }

}
