package com.gitcodings.stack.movies.rest;

import com.gitcodings.stack.core.result.MoviesResults;
import com.gitcodings.stack.movies.model.data.PersonDetail;
import com.gitcodings.stack.movies.model.request.PersonRequest;
import com.gitcodings.stack.movies.model.response.PersonByPersonIdResponse;
import com.gitcodings.stack.movies.model.response.PersonResponse;
import com.gitcodings.stack.movies.repo.MovieRepo;
import com.gitcodings.stack.movies.repo.PersonRepo;
import com.gitcodings.stack.movies.util.PersonService;
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
public class PersonController
{
    private final PersonRepo repo;
    private final Validate validate;
    private final PersonService personService;

    @Autowired
    public PersonController(PersonRepo repo, Validate validate, PersonService personService)
    {
        this.repo = repo;
        this.validate = validate;
        this.personService = personService;
    }

    // FUCK IT. Assume the authentication API is called here and authenticated

    @GetMapping("/person/search")
    public ResponseEntity<PersonResponse> personSearch(@AuthenticationPrincipal SignedJWT user,
                                                       PersonRequest request)
    {
        List<PersonDetail> persons = personService.searchPersons(request);

        PersonResponse response = new PersonResponse();
        if (persons == null || persons.isEmpty()) {
            response.setResult(MoviesResults.NO_PERSONS_FOUND_WITHIN_SEARCH);
        } else {
            response.setResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH)
                    .setPersons(persons);
        }

        return ResponseEntity.status(response.getResult().status())
                .body(response);
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
