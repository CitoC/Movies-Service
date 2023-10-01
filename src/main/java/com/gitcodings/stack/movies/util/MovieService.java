package com.gitcodings.stack.movies.util;

import com.gitcodings.stack.core.error.ResultError;
import com.gitcodings.stack.core.result.MoviesResults;
import com.gitcodings.stack.movies.model.data.Movie;
import com.gitcodings.stack.movies.model.request.MovieRequest;
import com.gitcodings.stack.movies.repo.MovieRepo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class MovieService {
    private final MovieRepo repo;

    public MovieService(MovieRepo repo) {
        this.repo = repo;
    }

    //
    public List<Movie> searchMovies(MovieRequest movie) {
        // lists of allowed options for various parameters
        List<String> validOrderBy = Arrays.asList("title", "year", "genre");   // apparently you cannot order by director
        List<String> validDirection = Arrays.asList("asce", "desc");
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
        return repo.getMovie(movie);
    }
}
