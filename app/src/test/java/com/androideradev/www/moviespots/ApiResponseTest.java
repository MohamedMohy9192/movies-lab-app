package com.androideradev.www.moviespots;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

@RunWith(JUnit4.class)
public class ApiResponseTest {

    @Test
    public void exception() {
        Exception exception = new Exception("error");
        ApiResponse.ApiErrorResponse<String> errorMessage = ApiResponse.create(exception);
        assertThat(errorMessage.getErrorMessage(), is("error"));
    }

    @Test
    public void emptyExceptionMessage_returnUnknownError() {
        Exception exception = new Exception("");
        ApiResponse.ApiErrorResponse<String> errorMessage = ApiResponse.create(exception);
        assertThat(errorMessage.getErrorMessage(), is("Unknown error"));
    }

    @Test
    public void success() {
        ApiResponse.ApiSuccessResponse<String> apiResponse = (ApiResponse.ApiSuccessResponse<String>) ApiResponse
                .create(Response.success("foo"));
        assertThat(apiResponse.getBody(), is("foo"));
    }

    @Test
    public void error() {
        // Create a synthetic error response with an HTTP status code of code and body as the error body.
        Response<String> errorResponse = Response.error(
                400,
        ResponseBody.create("blah", MediaType.parse("application/txt"))
        );
        ApiResponse.ApiErrorResponse<String> errorMessage = (ApiResponse.ApiErrorResponse<String>) ApiResponse.create(errorResponse);
        assertThat(errorMessage.getErrorMessage(), is("blah"));
    }

}