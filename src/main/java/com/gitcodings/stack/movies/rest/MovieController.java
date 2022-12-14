package com.gitcodings.stack.movies.rest;

import com.gitcodings.stack.movies.model.request.MovieByPersonIdRequest;
import com.gitcodings.stack.movies.model.request.MovieRequest;
import com.gitcodings.stack.movies.model.request.PersonRequest;
import com.gitcodings.stack.movies.model.response.MovieByMovieIdResponse;
import com.gitcodings.stack.movies.model.response.MovieResponse;
import com.gitcodings.stack.movies.model.response.PersonByPersonIdResponse;
import com.gitcodings.stack.movies.model.response.PersonResponse;
import com.gitcodings.stack.movies.repo.MovieRepo;
import com.gitcodings.stack.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;

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
        return null;
    }

    @GetMapping("/person/search")
    public ResponseEntity<PersonResponse> personSearch(@AuthenticationPrincipal SignedJWT user,
                                                       PersonRequest request)
    {
        return null;
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<PersonByPersonIdResponse> personSearchByPersonId(@AuthenticationPrincipal SignedJWT user,
                                                                           @PathVariable Long personId)
    {
        return null;
    }
}
