package com.androideradev.www.moviespots.data;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.androideradev.www.moviespots.LiveDataTestUtil;
import com.androideradev.www.moviespots.TestData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class MovieDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MoviesDatabase mMoviesDatabase;
    private MovieDao mMovieDao;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mMoviesDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                        MoviesDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        mMovieDao = mMoviesDatabase.moviesDao();
    }

    @After
    public void closeDb() {
        mMoviesDatabase.close();
    }

    @Test
    public void insertMovieAndGetById() throws InterruptedException {
        // GIVEN - Insert a Movie.
        DatabaseMovie movie = TestData.DATABASE_MOVIE1;
        mMovieDao.insertMovie(movie);

        // WHEN - Get the Movie by id from the database.
        DatabaseMovie loaded = LiveDataTestUtil.getValue(mMovieDao.getMovie(movie.getId()));

        // THEN - The loaded data contains the expected values.
        assertThat((DatabaseMovie) loaded, notNullValue());
        assertThat(loaded.getId(), is(movie.getId()));
        assertThat(loaded.getTitle(), is(movie.getTitle()));
        assertThat(loaded.getOverview(), is(movie.getOverview()));
        assertThat(loaded.isAdult(), is(movie.isAdult()));

    }

    @Test
    public void insertMovies_searchMoviesByTitle() throws InterruptedException {
        // GIVEN - Insert a Movies.
        List<DatabaseMovie> movies = TestData.MOVIES;
        mMovieDao.insertMovies(movies);

        // WHEN - Search the Movie by title from the database.
        List<DatabaseMovie> loaded = LiveDataTestUtil.getValue(mMovieDao.searchMovies("Dead"));

        // THEN - The loaded data contains the expected values.
        assertThat(loaded.size(), is(1));
        assertThat(loaded.get(0).getTitle(), is("House Dead"));


    }

    @Test
    public void insertMovies_retrieveAllMovies() throws InterruptedException {
        // GIVEN - Insert a Movies.
        List<DatabaseMovie> movies = TestData.MOVIES;
        mMovieDao.insertMovies(movies);

        // WHEN - Retrieve all movies from the database.
        List<DatabaseMovie> loaded = LiveDataTestUtil.getValue(mMovieDao.getAllMovies());

        // THEN - The loaded data contains the expected values.
        assertThat(loaded.size(), is(movies.size()));

    }

    @Test
    public void insertMovies_searchMoviesWithYear() throws InterruptedException {
        // GIVEN - Insert a Movies.
        List<DatabaseMovie> movies = TestData.MOVIES;
        mMovieDao.insertMovies(movies);

        // WHEN - Search the Movie by title with year from the database.
        List<DatabaseMovie> loaded =
                LiveDataTestUtil.getValue(mMovieDao.searchMoviesWithYear("Fast", "2000"));

        // THEN - The loaded data contains the expected values.
        assertThat(loaded.size(), is(2));
    }
}
