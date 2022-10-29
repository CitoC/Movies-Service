package com.gitcodings.stack.movies.model.response;

import com.gitcodings.stack.core.result.Result;
import com.gitcodings.stack.movies.model.data.PersonDetail;

public class PersonByPersonIdResponse {
    private Result result;
    private PersonDetail person;

    public Result getResult() {
        return result;
    }

    public PersonByPersonIdResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public PersonDetail getPerson() {
        return person;
    }

    public PersonByPersonIdResponse setPerson(PersonDetail person) {
        this.person = person;
        return this;
    }
}
