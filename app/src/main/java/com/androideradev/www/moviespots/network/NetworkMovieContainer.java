package com.androideradev.www.moviespots.network;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class NetworkMovieContainer {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<NetworkMovie> movies;

    @SerializedName("total_results")
    private int totalResults;

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setMovies(List<NetworkMovie> movies) {
        this.movies = movies;
    }

    public List<NetworkMovie> getMovies() {
        return movies;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalResults() {
        return totalResults;
    }
}