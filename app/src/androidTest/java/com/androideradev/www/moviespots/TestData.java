package com.androideradev.www.moviespots;

import com.androideradev.www.moviespots.data.DatabaseMovie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestData {


    public static final DatabaseMovie DATABASE_MOVIE1 = new DatabaseMovie(
            "Fast Car Five",
            "OriginalLanguage",
            "originalTitle",
            false,
            "Fast Car Five",
            genreIds(),
            "posterPath",
            "backdropPath",
            "2000-06-08",
            5.5,
            6.3,
            1,
            false,
            80);

    public static final DatabaseMovie DATABASE_MOVIE2 = new DatabaseMovie(
            "Fast People",
            "OriginalLanguage",
            "originalTitle",
            false,
            "Fast People",
            genreIds(),
            "posterPath",
            "backdropPath",
            "2000-06-06",
            5.5,
            6.3,
            2,
            false,
            80);

    public static final DatabaseMovie DATABASE_MOVIE3 = new DatabaseMovie(
            "House Dead",
            "OriginalLanguage",
            "originalTitle",
            false,
            "House Dead",
            genreIds(),
            "posterPath",
            "backdropPath",
            "2005",
            5.5,
            6.3,
            3,
            false,
            80);


    public final static List<DatabaseMovie> MOVIES = Arrays.asList(DATABASE_MOVIE1, DATABASE_MOVIE2, DATABASE_MOVIE3);

    private static List<Integer> genreIds() {
        List<Integer> genreIds = new ArrayList<>();
        genreIds.add(23);
        genreIds.add(55);
        genreIds.add(66);
        return genreIds;
    }
}
