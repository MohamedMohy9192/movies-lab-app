package com.androideradev.www.moviespots;

import java.io.IOException;

import retrofit2.Response;

/**
 * Read the raw responses from Retrofit and sign it a status one of
 * three possible status either the request was successful or was failed
 * i.e. there is an error or the request is empty which is still
 * successful response but nothing was returned
 *
 * @param <T>
 */
public class ApiResponse<T> {


    public static <T> ApiErrorResponse<T> create(Throwable error) {
        // If there is error return the error message
        // If there is an error and no message available return "unknown error" message
        return new ApiErrorResponse<>(
                error.getMessage() != null && !error.getMessage().isEmpty() ?
                        error.getMessage() :
                        "Unknown error");
    }

    public static <T> ApiResponse<T> create(Response<T> response) {
        // request was successful
        if (response.isSuccessful()) {
            T body = response.body();
            // handle the case if the request was Successful but the response content was empty
            if (body == null || response.code() == 204) { // 204 is empty reasoner code
                return new ApiEmptyResponse<>();
            } else {
                // if the response content not empty return success response
                return new ApiSuccessResponse<>(body);
            }
            // request was was not successful
        } else {
            String errorMessage = null;
            try {
                if (response.errorBody() != null) {
                    // The raw response body of an unsuccessful response.
                    // string() throws IOException which mean it cant convert the errorBody to a string
                    errorMessage = response.errorBody().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // HTTP status message or null if unknown.
                // catch the IOException and return the response status message
                errorMessage = response.message();
            }
            // if response status message null return Unknown error message
            return new ApiErrorResponse<>(errorMessage != null ? errorMessage : "Unknown error");
        }
    }

    /**
     * successful API response
     *
     * @param <T>
     */
    public static class ApiSuccessResponse<T> extends ApiResponse<T> {

        private final T body;

        public ApiSuccessResponse(T body) {
            this.body = body;
        }

        public T getBody() {
            return body;
        }
    }

    /**
     * Error API response
     *
     * @param <T>
     */
    public static class ApiErrorResponse<T> extends ApiResponse<T> {
        private final String errorMessage;

        public ApiErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * successful but empty API response
     *
     * @param <T>
     */
    public static class ApiEmptyResponse<T> extends ApiResponse<T> {
    }
}
