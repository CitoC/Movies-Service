package com.gitcodings.stack.movies.model.request;

public class MovieRequest {
    private String title;
    private Integer year;
    private String director;
    private String genre;
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;

    public String getTitle() {
        return title;
    }

    public MovieRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public MovieRequest setYear(Integer year) {
        this.year = year;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public MovieRequest setDirector(String director) {
        this.director = director;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public MovieRequest setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public MovieRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public MovieRequest setPage(Integer page) {
        this.page = page;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public MovieRequest setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public MovieRequest setDirection(String direction) {
        this.direction = direction;
        return this;
    }
}
