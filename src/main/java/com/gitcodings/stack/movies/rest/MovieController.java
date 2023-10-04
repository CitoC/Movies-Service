package com.gitcodings.stack.movies.rest;

import com.gitcodings.stack.core.result.MoviesResults;
import com.gitcodings.stack.core.security.JWTManager;
import com.gitcodings.stack.movies.model.data.*;
import com.gitcodings.stack.movies.model.request.MovieByPersonIdRequest;
import com.gitcodings.stack.movies.model.request.MovieRequest;
import com.gitcodings.stack.movies.model.response.MovieByMovieIdResponse;
import com.gitcodings.stack.movies.model.response.MovieResponse;
import com.gitcodings.stack.movies.repo.MovieRepo;
import com.gitcodings.stack.movies.util.MovieService;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
public class MovieController
{
    private final MovieRepo repo;
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieRepo repo, MovieService movieService)
    {
        this.repo = repo;
        this.movieService = movieService;
    }

    @GetMapping("/movie/search/person/{personId}")
    public ResponseEntity<MovieResponse> movieSearchByPersonId(@AuthenticationPrincipal SignedJWT user,
                                                               @PathVariable Long personId,
                                                               MovieByPersonIdRequest request) throws ParseException
    {
        // check the roles of user
        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        boolean canSeeHidden =  roles.contains("ADMIN") || roles.contains("EMPLOYEE");

        List<Movie> movies = movieService.searchMovies(personId, request, canSeeHidden);

        MovieResponse response = new MovieResponse();
        if (movies == null || movies.isEmpty()) {
            response.setResult(MoviesResults.NO_MOVIES_WITH_PERSON_ID_FOUND);
        } else {
            response.setResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND)
                    .setMovies(movies);
        }

        return ResponseEntity.status(response.getResult().status())
                .body(response);
    }

    @GetMapping("/movie/search")
    public ResponseEntity<MovieResponse> movieSearch(@AuthenticationPrincipal SignedJWT user,
                                                     MovieRequest request) throws ParseException
    {
        // check the roles of user
        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        boolean canSeeHidden =  roles.contains("ADMIN") || roles.contains("EMPLOYEE");

        List<Movie> movies = movieService.searchMovies(request, canSeeHidden);

        MovieResponse response = new MovieResponse();
        if (movies == null || movies.isEmpty()) {
            response.setResult(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH);
        } else {
            response.setResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH)
                    .setMovies(movies);
        }

        return ResponseEntity.status(response.getResult().status())
                .body(response);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieByMovieIdResponse> movieSearchByMovieId(@AuthenticationPrincipal SignedJWT user,
                                                                       @PathVariable Long movieId) throws ParseException {

        // check the roles of user
        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        boolean hasPrivilege =  roles.contains("ADMIN") || roles.contains("EMPLOYEE");

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

        // if movie is hidden and user has no privilege
        if (movie.getHidden() && !hasPrivilege) {
            MovieByMovieIdResponse response = new MovieByMovieIdResponse()
                    .setResult(MoviesResults.NO_MOVIE_WITH_ID_FOUND)
                    .setMovie(null)
                    .setGenres(null)
                    .setPersons(null);
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
