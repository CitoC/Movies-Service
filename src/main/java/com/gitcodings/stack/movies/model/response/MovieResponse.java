package com.gitcodings.stack.movies.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitcodings.stack.core.result.Result;
import com.gitcodings.stack.movies.model.data.Movie;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieResponse {
    private Result result;
    private List<Movie> movies;

    public Result getResult() {
        return result;
    }

    public MovieResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public MovieResponse setMovies(List<Movie> movies) {
        this.movies = movies;
        return this;
    }
}
