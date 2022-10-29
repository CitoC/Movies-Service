package com.gitcodings.stack.movies.model.request;

import com.gitcodings.stack.movies.model.response.PersonResponse;

public class PersonRequest {
    private String name;
    private String birthday;
    private String movieTitle;
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;

    public String getName() {
        return name;
    }

    public PersonRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public PersonRequest setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public PersonRequest setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public PersonRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public PersonRequest setPage(Integer page) {
        this.page = page;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public PersonRequest setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public PersonRequest setDirection(String direction) {
        this.direction = direction;
        return this;
    }
}
