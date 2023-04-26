package com.gitcodings.stack.movies.rest;

import com.gitcodings.stack.core.result.MoviesResults;
import com.gitcodings.stack.movies.model.data.PersonDetail;
import com.gitcodings.stack.movies.model.request.PersonRequest;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController
{
    private final MovieRepo repo;
    private final Validate validate;

    @Autowired
    public PersonController(MovieRepo repo, Validate validate)
    {
        this.repo = repo;
        this.validate = validate;
    }

    // FUCK IT. Assume the authentication API is called here and authenticated

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
        // SignedJWT is being intentionally omitted.

        // go into database and search for the record with personId
        // if found return PersonDetail, if not return null
        PersonDetail person = repo.selectPersonDetail(personId);

        // person is not found
        if (person == null)
        {
            PersonByPersonIdResponse response = new PersonByPersonIdResponse()
                    .setResult(MoviesResults.NO_PERSON_WITH_ID_FOUND);

            return ResponseEntity
                    .status(response.getResult().status())
                    .body(response);
        }

        // person is found
        PersonByPersonIdResponse response = new PersonByPersonIdResponse()
                .setResult(MoviesResults.PERSON_WITH_ID_FOUND)
                .setPerson(person);

        return ResponseEntity
                .status(response.getResult().status())
                .body(response);
    }
}
