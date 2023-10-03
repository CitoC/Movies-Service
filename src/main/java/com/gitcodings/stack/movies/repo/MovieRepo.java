package com.gitcodings.stack.movies.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitcodings.stack.movies.model.data.*;
import com.gitcodings.stack.movies.model.request.MovieRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
@Repository
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

    public MovieDetail selectMovieDetail(Long movieId)
    {
        MovieDetail movie = null;
        try {
            movie = this.template.queryForObject(
                    "SELECT m.id, m.title, m.year, p.name, m.rating, m.num_votes, m.budget, m.revenue, m.overview, m.backdrop_path, m.poster_path, m.hidden " +
                    "FROM movies.movie AS m " +
                    "INNER JOIN movies.person AS p ON p.id = m.director_id " +
                    "WHERE m.id = :movieId",

                    new MapSqlParameterSource()
                            .addValue("movieId", movieId, Types.INTEGER),

                    (rs, rowNum) ->
                            new MovieDetail()
                                    .setId(rs.getLong("id"))
                                    .setTitle(rs.getString("title"))
                                    .setYear(rs.getInt("year"))
                                    .setDirector(rs.getString("name"))
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
                    "SELECT JSON_ARRAYAGG(JSON_OBJECT('id', g.id, 'name', g.name)) as jsonArrayString " +
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
                    "SELECT JSON_ARRAYAGG(JSON_OBJECT('id', p.id, 'name', p.name)) as jsonArrayString " +
                            "FROM movies.person p " +
                            "   JOIN movies.movie_person mp ON mp.person_id = p.id " +
                            "WHERE mp.movie_id = :movieId",
                    new MapSqlParameterSource()
                            .addValue("movieId", movieId, Types.INTEGER),

                    (rs, rowNum) ->
                            new String(rs.getString("jsonArrayString"))
            );

            System.out.println(personJsonArray);

            Person[] personArray = objectMapper.readValue(personJsonArray, Person[].class);
            persons = Arrays.stream(personArray).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException | JsonProcessingException e) {
            return null;
        }

        return persons;
    }

    public List<PersonDetail> selectMoviePersonDetail(Long movieId)
    {
        List<PersonDetail> persons;
        try {
            String personJsonArray = this.template.queryForObject(
                    "SELECT JSON_ARRAYAGG(JSON_OBJECT('id', p.id, 'name', p.name, 'birthday', p.birthday, 'biography', p.biography, 'birthplace', p.birthday, 'popularity', p.popularity, 'profilePath', p.profile_path)) as jsonArrayString " +
                            "FROM movies.person p " +
                            "   JOIN movies.movie_person mp ON p.id = mp.person_id " +
                            "WHERE mp.movie_id = :movieId",
                    new MapSqlParameterSource()
                            .addValue("movieId", movieId, Types.INTEGER),

                    (rs, rowNum) ->
                            new String(rs.getString("jsonArrayString"))
            );

            PersonDetail[] personArray = objectMapper.readValue(personJsonArray, PersonDetail[].class);
            persons = Arrays.stream(personArray).collect(Collectors.toList());

        } catch (EmptyResultDataAccessException | JsonProcessingException e) {
            return null;
        }

        return persons;
    }

    public List<Movie> getMovie(MovieRequest movie) {
        final int DEFAULT_LIMIT = 10;
        final int DEFAULT_PAGE = 0;
        List<Movie> movies = new ArrayList<>();
        String titleQuery = "";
        String yearQuery = "";
        String directorQuery = "";
        String genreQuery = "";
        String limitQuery = "LIMIT " + Integer.toString(DEFAULT_LIMIT) + " ";
        String pageQuery = "OFFSET " + Integer.toString(DEFAULT_PAGE) + " ";
        String orderByQuery = "ORDER BY m.title ";
        String directionQuery = "ASC ";

        if (movie.getTitle() != null && !movie.getTitle().trim().isEmpty()) {
            titleQuery = movie.getTitle();
        }

        if (movie.getYear() != null) {
            yearQuery = movie.getYear() + " ";  // getYear() returns Integer but implicit conversion occurs here
        }

        if (movie.getDirector() != null && !movie.getDirector().trim().isEmpty()) {
            directorQuery = movie.getDirector();
        }

        if (movie.getGenre() != null && !movie.getGenre().trim().isEmpty()) {
            genreQuery = movie.getGenre();
        }

        if (movie.getLimit() != null) {
            limitQuery = "LIMIT " + Integer.toString(movie.getLimit()) + " ";
        }

        if (movie.getPage() != null) {
            int limit = (movie.getLimit() == null) ? DEFAULT_LIMIT : movie.getLimit();
            int offset = (movie.getPage() - 1) * limit;  // offset calculation
            pageQuery = "OFFSET " + Integer.toString(offset) + " ";
        }

        if (movie.getOrderBy() != null) {
            orderByQuery = "ORDER BY " + movie.getOrderBy() + " ";
        }

        if (movie.getDirection() != null) {
            directionQuery = "";    // clear the default value
            directionQuery = movie.getDirection() + " ";
        }

        // Only include the WHERE clause if at least one search term is provided
        // A note about the whereClause is that the table aliases are hard-coded. An alternate
        // way to this is maybe use string interpolation with some final
        String whereClause = "";
        if (!titleQuery.isEmpty() || !yearQuery.isEmpty() || !directorQuery.isEmpty() || !genreQuery.isEmpty()) {
            whereClause = "WHERE ";
            if (!titleQuery.trim().isEmpty()) {
                whereClause += "m.title LIKE '%" + titleQuery + "%' AND ";
            }
            if (!yearQuery.isEmpty()) {
                whereClause += "m.year = " + yearQuery + " AND ";
            }
            if (!directorQuery.isEmpty()) {
                whereClause += "p.name LIKE '%" + directorQuery + "%' AND ";
            }
            if (!genreQuery.isEmpty()) {
                whereClause += "g.name LIKE '%" + genreQuery + "%' AND ";
            }
            whereClause = whereClause.substring(0, whereClause.length() - 5); // Remove the last "AND"
        }

        // INSANE sub-query solution for executing ORDER BY and LIMIT before SELECT.
        // Basically perform a sub-query with all the WHERE and additional clauses,
        // and then from that sub-query, SELECT using the JSONARRAYARG.
        // Also added secondary sorting after orderByQuery.
        // A note about all the JOINs, I'm joining ALL the tables here which is definitely
        // tanking the performance. An alternative way I have thought about is to
        // use some kind of conditionals. So for example, if the query contains the genre,
        // only then we will do a Genre table join. Similarly, only if the query contains
        // the director, we will do a Person table join.
        // IMPORTANT: DO NOT CHANGE THE TABLE ALIASES
        String run = "SELECT JSON_ARRAYAGG(JSON_OBJECT( " +
                "'id', subquery.id, " +
                "'title', subquery.title, " +
                "'director', subquery.director, " +
                "'rating', subquery.rating, " +
                "'year', subquery.year, " +
                "'backdropPath', subquery.backdrop_path, " +
                "'posterPath', subquery.poster_path, " +
                "'hidden', subquery.hidden " +
                ")) as jsonArrayString " +
                "FROM ( " +
                " SELECT DISTINCT m.id, m.title, m.year, p.name AS director, m.rating, m.backdrop_path, m.poster_path, m.hidden " +
                " FROM movies.movie AS m " +
                " JOIN movies.person AS p ON p.id = m.director_id " +
                " JOIN movies.movie_genre AS mg ON m.id = mg.movie_id " +
                " JOIN movies.genre AS g ON mg.genre_id = g.id " +
                whereClause +
                orderByQuery + directionQuery + ", m.id " + limitQuery + pageQuery +
                ") AS subquery";


        try {
            String moviesJsonArray = this.template.queryForObject(
                    run,
                    new MapSqlParameterSource()
                            .addValue("", 10  , Types.INTEGER),
                    (rs, rowNum) ->
                            new String(rs.getString("jsonArrayString"))
            );

            Movie[] moviesArray = objectMapper.readValue(moviesJsonArray, Movie[].class);
            movies = Arrays.stream(moviesArray).collect(Collectors.toList());

            // added NullPointerException because NullPointerException is being thrown for the test case moviesSearchNoneFound
        } catch (NullPointerException | EmptyResultDataAccessException | JsonProcessingException e) {
            return null;
        }
        return movies;
    }
}
