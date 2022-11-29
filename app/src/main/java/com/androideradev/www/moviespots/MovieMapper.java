package com.androideradev.www.moviespots;

import com.androideradev.www.moviespots.data.DatabaseMovie;
import com.androideradev.www.moviespots.network.NetworkMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {

    public static List<DatabaseMovie> toDatabaseMovies(List<NetworkMovie> movies) {
        return movies.stream().map(networkMovie ->
                        new DatabaseMovie(networkMovie.getOverview(), networkMovie.getOriginalLanguage(), networkMovie.getOriginalTitle(), networkMovie.isVideo(), networkMovie.getTitle(), networkMovie.getGenreIds(), networkMovie.getPosterPath(), networkMovie.getBackdropPath(), networkMovie.getReleaseDate(), networkMovie.getPopularity(), networkMovie.getVoteAverage(), networkMovie.getId(), networkMovie.isAdult(), networkMovie.getVoteCount()))
                .collect(Collectors.toList());
    }

    public static List<Movie> toMovies(List<DatabaseMovie> databaseMovies) {
        return databaseMovies.stream().map(movie -> new Movie(movie.getId(), movie.getTitle(), movie.getPosterPath(), movie.getReleaseDate(), movie.getVoteAverage())).collect(Collectors.toList());
    }
}
