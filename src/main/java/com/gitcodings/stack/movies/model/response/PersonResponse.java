package com.gitcodings.stack.movies.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitcodings.stack.core.result.Result;
import com.gitcodings.stack.movies.model.data.Person;
import com.gitcodings.stack.movies.model.data.PersonDetail;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonResponse {
    private Result result;
    private List<PersonDetail> persons;

    public Result getResult() {
        return result;
    }

    public PersonResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public List<PersonDetail> getPersons() {
        return persons;
    }

    public PersonResponse setPersons(List<PersonDetail> persons) {
        this.persons = persons;
        return this;
    }
}
