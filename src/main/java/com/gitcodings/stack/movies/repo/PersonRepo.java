package com.gitcodings.stack.movies.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitcodings.stack.movies.model.data.PersonDetail;
import com.gitcodings.stack.movies.model.request.PersonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
@Repository
public class PersonRepo {
    private final NamedParameterJdbcTemplate template;
    private final ObjectMapper objectMapper;

    @Autowired
    public PersonRepo(NamedParameterJdbcTemplate template, ObjectMapper objectMapper) {
        this.template = template;
        this.objectMapper = objectMapper;
    }

    // search the person with the personId and return a PersonDetail
    // return null if no row is found
    public PersonDetail selectPersonDetail(Long personId)
    {
        try {
            PersonDetail person = this.template.queryForObject(
                    "SELECT id, name, birthday, biography, birthplace, popularity, profile_path " +
                            "FROM movies.person " +
                            "WHERE id = :personId",

                    new MapSqlParameterSource()
                            .addValue("personId", personId, Types.INTEGER),

                    (rs, rowNum) ->
                            new PersonDetail()
                                    .setId(rs.getLong("id"))
                                    .setName(rs.getString("name"))
                                    .setBirthday(rs.getString("birthday"))
                                    .setBiography(rs.getString("biography"))
                                    .setBirthplace(rs.getString("birthplace"))
                                    .setPopularity(rs.getFloat("popularity"))
                                    .setProfilePath(rs.getString("profile_path"))
            );
            return person;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<PersonDetail> getPerson(PersonRequest person) {
        final int DEFAULT_LIMIT = 10;
        final int DEFAULT_PAGE = 0;
        List<PersonDetail> persons = new ArrayList<>();
        String nameQuery = "";
        String birthdayQuery = "";
        String movieTitleQuery = "";
        String limitQuery = "LIMIT " + DEFAULT_LIMIT + " ";
        String pageQuery = "OFFSET " + DEFAULT_PAGE + " ";
        String orderByQuery = "ORDER BY p.name ";
        String directionQuery = "ASC ";

        if (person.getName() != null && !person.getName().trim().isEmpty()) {
            nameQuery = person.getName();
        }

        if (person.getBirthday() != null && !person.getBirthday().trim().isEmpty()) {
            birthdayQuery = person.getBirthday();
        }

        if (person.getMovieTitle() != null && !person.getMovieTitle().trim().isEmpty()) {
            movieTitleQuery = person.getMovieTitle();
        }

        if (person.getLimit() != null) {
            limitQuery = "LIMIT " + Integer.toString(person.getLimit()) + " ";
        }

        if (person.getPage() != null) {
            int limit = (person.getLimit() == null) ? DEFAULT_LIMIT : person.getLimit();
            int offset = (person.getPage() - 1) * limit;  // offset calculation
            pageQuery = "OFFSET " + Integer.toString(offset) + " ";
        }

        if (person.getOrderBy() != null) {
            orderByQuery = "ORDER BY " + person.getOrderBy() + " ";
        }

        if (person.getDirection() != null) {
            directionQuery = "";    // clear the default value
            directionQuery = person.getDirection() + " ";
        }

        String whereClause = "";
        if (!nameQuery.isEmpty() || !birthdayQuery.isEmpty() || !movieTitleQuery.isEmpty()) {
            whereClause = "WHERE ";
            if (!nameQuery.isEmpty()) {
                whereClause += "p.name LIKE '%" + nameQuery + "%' AND ";
            }
            if (!birthdayQuery.isEmpty()) {
                whereClause += "p.birthday LIKE '%" + birthdayQuery + "%' AND ";
            }
            if (!movieTitleQuery.isEmpty()) {
                whereClause += "m.title LIKE '%" + movieTitleQuery + "%' AND ";
            }
            whereClause = whereClause.substring(0, whereClause.length() - 5);
        }

        String run = "SELECT JSON_ARRAYAGG(JSON_OBJECT( " +
                "'id', subquery.id, " +
                "'name', subquery.name, " +
                "'birthday', subquery.birthday, " +
                "'biography', subquery.biography, " +
                "'birthplace', subquery.birthplace, " +
                "'popularity', subquery.popularity, " +
                "'profilePath', subquery.profile_path " +
                ")) as jsonArrayString " +
                "FROM ( " +
                " SELECT DISTINCT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path " +
                " FROM movies.person AS p " +
                " JOIN movies.movie_person AS mp ON mp.person_id = p.id " +
                " JOIN movies.movie AS m ON m.id = mp.movie_id " +
                whereClause +
                orderByQuery + directionQuery + ", p.id " + limitQuery + pageQuery +
                ") AS subquery";
        System.out.println(run);

        try {
            String personsJsonArray = this.template.queryForObject(
                    run,
                    new MapSqlParameterSource()
                            .addValue("", null, Types.INTEGER),
                    (rs, rowNum) ->
                            new String(rs.getString("jsonArrayString"))
            );

            PersonDetail[] personsArray = objectMapper.readValue(personsJsonArray, PersonDetail[].class);
            persons = Arrays.stream(personsArray).collect(Collectors.toList());
        } catch (NullPointerException | EmptyResultDataAccessException | JsonProcessingException e) {
            return null;
        }
        return persons;
    }
}
