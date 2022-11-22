package com.androideradev.www.moviespots.network;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.androideradev.www.moviespots.ApiResponse;
import com.androideradev.www.moviespots.LiveDataCallAdapterFactory;
import com.androideradev.www.moviespots.util.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;

import kotlin.text.Charsets;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(JUnit4.class)
public class MoviesApiTest {
    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private MoviesApi mMoviesApi;

    private MockWebServer mockWebServer;

    @Before
    public void createService() {
        mockWebServer = new MockWebServer();
        mMoviesApi = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(MoviesApi.class);
    }

    @After
    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void requestMovieById() throws IOException, InterruptedException {
        enqueue("movie-id-384018.json");

        ApiResponse.ApiSuccessResponse<NetworkMovie> response =
                (ApiResponse.ApiSuccessResponse<NetworkMovie>) LiveDataTestUtil.getValue(
                        mMoviesApi.getMovieById(384018,
                                NetworkUtilities.API_KEY,
                                "en-US"));

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("/movie/384018?api_key=" + NetworkUtilities.API_KEY + "&language=en-US"));

        NetworkMovie movie = response.getBody();
        assertThat(movie, notNullValue());
        assertThat(movie.getId(), is(384018));
        assertThat(movie.getTitle(), is("Fast & Furious Presents: Hobbs & Shaw"));
        assertThat(movie.getBackdropPath(), is("/hpgda6P9GutvdkDX5MUJ92QG9aj.jpg"));
    }

    @Test
    public void search() throws IOException, InterruptedException {
        enqueue("search.json");
        ApiResponse.ApiSuccessResponse<NetworkMovieContainer> response =
                (ApiResponse.ApiSuccessResponse<NetworkMovieContainer>) LiveDataTestUtil.getValue(
                        mMoviesApi.searchMovie(NetworkUtilities.API_KEY,
                                "Fast",
                                1,
                                "en-US"));

        assertThat(response, notNullValue());
        assertThat(response.getBody().getTotalResults(), is(552));
        assertThat(response.getBody().getMovies().size(), is(20));
        int firstItemIndex = 0;
        NetworkMovie movie = response.getBody().getMovies().get(firstItemIndex);
        assertThat(movie.getTitle(), is("Fast X"));
        assertThat(movie.getOverview(), is("The tenth installment in the Fast Saga."));
        assertThat(movie.getPopularity(), is(97.027));
        assertThat(movie.getId(), is(385687));
    }

    /**
     * The function takes a file from our test resources and turns it into a fake API response.
     */
    private void enqueue(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("api-response/" + fileName);

        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();

        mockWebServer.enqueue(
                mockResponse.setBody(source.readString(Charsets.UTF_8))
        );
    }
}