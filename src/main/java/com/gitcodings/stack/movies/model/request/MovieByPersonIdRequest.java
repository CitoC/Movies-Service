package com.gitcodings.stack.movies.model.request;

// Retrospective: what if I create a queryOptionsRequest and let other classes (such as MovieRequest)
public class MovieByPersonIdRequest {
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;

    public Integer getLimit() {
        return limit;
    }

    public MovieByPersonIdRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public MovieByPersonIdRequest setPage(Integer page) {
        this.page = page;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public MovieByPersonIdRequest setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public MovieByPersonIdRequest setDirection(String direction) {
        this.direction = direction;
        return this;
    }
}
