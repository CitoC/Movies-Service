package com.gitcodings.stack.movies.model.data;

public class Person {
    private Long personId;
    private String name;

    public Long getPersonId() {
        return personId;
    }

    public Person setPersonId(Long personId) {
        this.personId = personId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }
}
