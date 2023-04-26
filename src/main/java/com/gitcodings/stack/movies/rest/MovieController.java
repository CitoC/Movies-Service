package com.gitcodings.stack.movies.rest;

import com.gitcodings.stack.core.result.MoviesResults;
import com.gitcodings.stack.movies.model.data.Genre;
import com.gitcodings.stack.movies.model.data.MovieDetail;
import com.gitcodings.stack.movies.model.data.Person;
import com.gitcodings.stack.movies.model.request.MovieByPersonIdRequest;
import com.gitcodings.stack.movies.model.request.MovieRequest;
import com.gitcodings.stack.movies.model.response.MovieByMovieIdResponse;
import com.gitcodings.stack.movies.model.response.MovieResponse;
import com.gitcodings.stack.movies.repo.MovieRepo;
import com.gitcodings.stack.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController
{
    private final MovieRepo repo;
    private final Validate validate;

    @Autowired
    public MovieController(MovieRepo repo, Validate validate)
    {
        this.repo = repo;
        this.validate = validate;
    }

    @GetMapping("/movie/search")
    public ResponseEntity<MovieResponse> movieSearch(@AuthenticationPrincipal SignedJWT user,
                                                     MovieRequest request)
    {
        return null;
    }

    @GetMapping("/movie/search/person/{personId}")
    public ResponseEntity<MovieResponse> movieSearchByPersonId(@AuthenticationPrincipal SignedJWT user,
                                                               @PathVariable Long personId,
                                                               MovieByPersonIdRequest request)
    {
        return null;
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieByMovieIdResponse> movieSearchByMovieId(@AuthenticationPrincipal SignedJWT user,
                                                                       @PathVariable Long movieId)
    {
        // SignedJWT is being intentionally omitted
        // Is this for role?

        // go into database and search for the record with movieId
        // if found return MovieDetail, if not return null
        MovieDetail movie = repo.selectMovieDetail(movieId);

        // movie is not found
        if (movie == null)
        {
            MovieByMovieIdResponse response = new MovieByMovieIdResponse()
                    .setResult(MoviesResults.NO_MOVIE_WITH_ID_FOUND);

            return ResponseEntity
                    .status(response.getResult().status())
                    .body(response);
        }

        // movie is found
        // query in database for other infos
        // genres and persons could be missing (null)
        List<Genre> genres = repo.selectMovieGenres(movieId);
        List<Person> persons = repo.selectMoviePersons(movieId);

        MovieByMovieIdResponse response = new MovieByMovieIdResponse()
                .setResult(MoviesResults.MOVIE_WITH_ID_FOUND)
                .setMovie(movie)
                .setGenres(genres)
                .setPersons(persons);

        return ResponseEntity
                .status(response.getResult().status())
                .body(response);
    }
}
