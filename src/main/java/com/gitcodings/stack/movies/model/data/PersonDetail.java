package com.gitcodings.stack.movies.model.data;

public class PersonDetail {
    private Long personId;
    private String name;
    private String birthday = null;
    private String biography = null;
    private String birthplace = null;
    private Float popularity = null;
    private String profilePath = null;

    public Long getPersonId() {
        return personId;
    }

    public PersonDetail setPersonId(Long personId) {
        this.personId = personId;
        return this;
    }

    public String getName() {
        return name;
    }

    public PersonDetail setName(String name) {
        this.name = name;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public PersonDetail setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getBiography() {
        return biography;
    }

    public PersonDetail setBiography(String biography) {
        this.biography = biography;
        return this;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public PersonDetail setBirthplace(String birthplace) {
        this.birthplace = birthplace;
        return this;
    }

    public Float getPopularity() {
        return popularity;
    }

    public PersonDetail setPopularity(Float popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public PersonDetail setProfilePath(String profilePath) {
        this.profilePath = profilePath;
        return this;
    }
}
