package com.gitcodings.stack.movies.util;

import com.gitcodings.stack.core.error.ResultError;
import com.gitcodings.stack.core.result.MoviesResults;
import com.gitcodings.stack.movies.model.data.Movie;
import com.gitcodings.stack.movies.model.request.MovieByPersonIdRequest;
import com.gitcodings.stack.movies.model.request.MovieRequest;
import com.gitcodings.stack.movies.repo.MovieRepo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepo repo;

    public MovieService(MovieRepo repo) {
        this.repo = repo;
    }

    //
    public List<Movie> searchMovies(MovieRequest movie, boolean canSeeHidden) {
        // lists of allowed options for various parameters
        List<String> validOrderBy = Arrays.asList("title", "year", "genre", "rating");   // apparently you cannot order by director
        List<String> validDirection = Arrays.asList("asc", "desc");
        List<Integer> validLimit = Arrays.asList(10,25,50,100);

        // Check if the orderBy value is valid by comparing it to a list of allowed options using a stream
        if (movie.getOrderBy() != null &&
                validOrderBy.stream().noneMatch(option -> option.equals(movie.getOrderBy().toLowerCase(Locale.ROOT)))) {
            throw new ResultError(MoviesResults.INVALID_ORDER_BY);
        }

        if (movie.getDirection() != null &&
                validDirection.stream().noneMatch(option -> option.equals(movie.getDirection().toLowerCase(Locale.ROOT)))) {
            throw new ResultError(MoviesResults.INVALID_DIRECTION);
        }

        if (movie.getLimit() != null &&
                validLimit.stream().noneMatch(option -> option.equals(movie.getLimit()))) {
            throw new ResultError(MoviesResults.INVALID_LIMIT);
        }

        if (movie.getPage() != null &&
                movie.getPage() <= 0) {
            throw new ResultError(MoviesResults.INVALID_PAGE);
        }

        // Error-checking done from this point
        return repo.getMovie(movie, canSeeHidden);
    }

    public List<Movie> searchMovies(Long personId, MovieByPersonIdRequest request, boolean canSeeHidden) {
        // lists of allowed options for various parameters
        List<String> validOrderBy = Arrays.asList("title", "year", "rating");   // apparently you cannot order by director
        List<String> validDirection = Arrays.asList("asc", "desc");
        List<Integer> validLimit = Arrays.asList(10,25,50,100);

        // Check if the orderBy value is valid by comparing it to a list of allowed options using a stream
        if (request.getOrderBy() != null &&
                validOrderBy.stream().noneMatch(option -> option.equals(request.getOrderBy().toLowerCase(Locale.ROOT)))) {
            throw new ResultError(MoviesResults.INVALID_ORDER_BY);
        }

        if (request.getDirection() != null &&
                validDirection.stream().noneMatch(option -> option.equals(request.getDirection().toLowerCase(Locale.ROOT)))) {
            throw new ResultError(MoviesResults.INVALID_DIRECTION);
        }

        if (request.getLimit() != null &&
                validLimit.stream().noneMatch(option -> option.equals(request.getLimit()))) {
            throw new ResultError(MoviesResults.INVALID_LIMIT);
        }

        if (request.getPage() != null &&
                request.getPage() <= 0) {
            throw new ResultError(MoviesResults.INVALID_PAGE);
        }

        // Error-checking done from this point
        return repo.getMovie(personId, request, canSeeHidden);
    }

    public List<Movie> filterHidden(List<Movie> movies) {
        return movies.stream().filter(movie -> !movie.getHidden()).collect(Collectors.toList());
    }
}
