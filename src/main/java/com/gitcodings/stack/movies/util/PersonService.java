package com.gitcodings.stack.movies.util;

import com.gitcodings.stack.core.error.ResultError;
import com.gitcodings.stack.core.result.MoviesResults;
import com.gitcodings.stack.movies.model.data.PersonDetail;
import com.gitcodings.stack.movies.model.request.PersonRequest;
import com.gitcodings.stack.movies.repo.PersonRepo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class PersonService {
    public final PersonRepo repo;

    public PersonService(PersonRepo repo) {
        this.repo = repo;
    }

    public List<PersonDetail> searchPersons(PersonRequest person) {
        List<String> validOrderBy = Arrays.asList("name", "popularity", "birthday");
        List<String> validDirection = Arrays.asList("asc", "desc");
        List<Integer> validLimit = Arrays.asList(10,25,50,100);

        if (person.getOrderBy() != null &&
                validOrderBy.stream().noneMatch(option -> option.equals(person.getOrderBy().toLowerCase(Locale.ROOT)))) {
            throw new ResultError(MoviesResults.INVALID_ORDER_BY);
        }

        if (person.getDirection() != null &&
                validDirection.stream().noneMatch(option -> option.equals(person.getDirection().toLowerCase(Locale.ROOT)))) {
            throw new ResultError(MoviesResults.INVALID_DIRECTION);
        }

        if (person.getLimit() != null &&
                validLimit.stream().noneMatch(option -> option.equals(person.getLimit()))) {
            throw new ResultError(MoviesResults.INVALID_LIMIT);
        }

        if (person.getPage() != null && person.getPage() <= 0) {
            throw new ResultError(MoviesResults.INVALID_PAGE);
        }

        // Error-checking done from this point
        return repo.getPerson(person);
    }
}
