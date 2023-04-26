package com.gitcodings.stack.movies.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitcodings.stack.movies.model.data.Genre;
import com.gitcodings.stack.movies.model.data.MovieDetail;
import com.gitcodings.stack.movies.model.data.Person;
import com.gitcodings.stack.movies.model.data.PersonDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieRepo
{
    private final NamedParameterJdbcTemplate template;
    private final ObjectMapper objectMapper;

    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template)
    {
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
                                    .setPersonId(rs.getLong("id"))
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

    public MovieDetail selectMovieDetail(Long movieId)
    {
        try {
            MovieDetail movie = this.template.queryForObject(
                    "SELECT id, title, year, director_id, rating, num_votes, budget, revenue, overview, backdrop_path, poster_path, hidden " +
                    "FROM movies.movie " +
                    "WHERE id = :movieId",

                    new MapSqlParameterSource()
                            .addValue("movieId", movieId, Types.INTEGER),

                    (rs, rowNum) ->
                            new MovieDetail()
                                    .setId(rs.getLong("id"))
                                    .setTitle(rs.getString("title"))
                                    .setYear(rs.getInt("year"))
                                    .setDirector(rs.getString("director_id")) // TODO: director name, not id
                                    .setRating(rs.getDouble("rating"))
                                    .setNumVotes(rs.getLong("num_votes"))
                                    .setBudget(rs.getLong("budget"))
                                    .setRevenue(rs.getLong("revenue"))
                                    .setOverview(rs.getString("overview"))
                                    .setBackdropPath(rs.getString("backdrop_path"))
                                    .setPosterPath(rs.getString("poster_path"))
                                    .setHidden(rs.getBoolean("hidden"))
            );
            return movie;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<Genre> selectMovieGenres(Long movieId) {
        List<Genre> genres;
        try {
            String genreJsonArray = this.template.queryForObject(
                    "SELECT JSON_ARRAYAGG(JSON_OBJECT('genreId', g.id, 'name', g.name)) as jsonArrayString " +
                            "FROM movies.genre g " +
                            "   JOIN movies.movie_genre mg ON g.id = mg.genre_id " +
                            "WHERE mg.movie_id = :movieId",
                    new MapSqlParameterSource()
                            .addValue("movieId", movieId, Types.INTEGER),

                    (rs, rowNum) ->
                            new String(rs.getString("jsonArrayString"))
            );

            Genre[] genreArray = objectMapper.readValue(genreJsonArray, Genre[].class);
            genres = Arrays.stream(genreArray).collect(Collectors.toList());

        } catch (EmptyResultDataAccessException | JsonProcessingException e) {
            return null;
        }

        return genres;
    }

    public List<Person> selectMoviePersons(Long movieId)
    {
        List<Person> persons;
        try {
            String personJsonArray = this.template.queryForObject(
                    "SELECT JSON_ARRAYAGG(JSON_OBJECT('personId', p.id, 'name', p.name)) as jsonArrayString " +
                            "FROM movies.person p " +
                            "   JOIN movies.movie_person mp ON p.id = mp.person_id " +
                            "WHERE mp.movie_id = :movieId",
                    new MapSqlParameterSource()
                            .addValue("movieId", movieId, Types.INTEGER),

                    (rs, rowNum) ->
                            new String(rs.getString("jsonArrayString"))
            );

            Person[] personArray = objectMapper.readValue(personJsonArray, Person[].class);
            persons = Arrays.stream(personArray).collect(Collectors.toList());

        } catch (EmptyResultDataAccessException | JsonProcessingException e) {
            return null;
        }

        return persons;
    }
}
