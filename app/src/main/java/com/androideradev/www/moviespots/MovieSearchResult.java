package com.androideradev.www.moviespots;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "movie_search_result")
public class MovieSearchResult {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "query_text")
    private String query;
    @ColumnInfo(name = "movie_ids")
    private List<Integer> movieIds;
    @ColumnInfo(name = "page_number")
    private Integer nextPageNumber;
    @ColumnInfo(name = "total_pages")
    private int totalPages;
    @ColumnInfo(name = "total_results")
    private int totalResults;


    public MovieSearchResult(@NonNull String query, List<Integer> movieIds, Integer nextPageNumber, int totalPages, int totalResults) {
        this.query = query;
        this.movieIds = movieIds;
        this.nextPageNumber = nextPageNumber;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    @NonNull
    public String getQuery() {
        return query;
    }

    public void setQuery(@NonNull String query) {
        this.query = query;
    }

    public List<Integer> getMovieIds() {
        return movieIds;
    }

    public void setMovieIds(List<Integer> movieIds) {
        this.movieIds = movieIds;
    }

    public Integer getNextPageNumber() {
        return nextPageNumber;
    }

    public void setNextPageNumber(Integer nextPageNumber) {
        this.nextPageNumber = nextPageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
