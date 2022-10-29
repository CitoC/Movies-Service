package com.gitcodings.stack.movies.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitcodings.stack.core.result.Result;
import com.gitcodings.stack.movies.model.data.Genre;
import com.gitcodings.stack.movies.model.data.MovieDetail;
import com.gitcodings.stack.movies.model.data.Person;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieByMovieIdResponse {
    private Result result;
    private MovieDetail movie;
    private List<Genre> genres;
    private List<Person> persons;

    public Result getResult() {
        return result;
    }

    public MovieByMovieIdResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public MovieDetail getMovie() {
        return movie;
    }

    public MovieByMovieIdResponse setMovie(MovieDetail movie) {
        this.movie = movie;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public MovieByMovieIdResponse setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public MovieByMovieIdResponse setPersons(List<Person> persons) {
        this.persons = persons;
        return this;
    }
}
