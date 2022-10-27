package com.gitcodings.stack.movies.rest;

import com.gitcodings.stack.movies.repo.MovieRepo;
import com.gitcodings.stack.movies.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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
}
