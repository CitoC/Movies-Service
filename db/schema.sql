CREATE SCHEMA IF NOT EXISTS movies;

CREATE TABLE IF NOT EXISTS movies.genre
(
    id INT NOT NULL PRIMARY KEY ,
    name VARCHAR(32) NOT NULL
);

-- DECIMAL is equivalent as DECIMAL(10,0)
-- the decimal parts were omitted
-- Fixed with a python script
CREATE TABLE IF NOT EXISTS movies.person
(
    id INT NOT NULL PRIMARY KEY ,
    name VARCHAR(128) NOT NULL ,
    birthday DATE NULL ,
    biography VARCHAR(8192) NULL ,
    birthplace VARCHAR(128) NULL ,
    popularity DECIMAL NULL ,
    profile_path VARCHAR(32) NULL
);

-- Same problem as above Person table
CREATE TABLE IF NOT EXISTS movies.movie
(
    id INT NOT NULL PRIMARY KEY ,
    title VARCHAR(128) NOT NULL ,
    year INT NOT NULL ,
    director_id INT NOT NULL ,
    rating DECIMAL NOT NULL DEFAULT 0.0 ,
    num_votes INT NOT NULL DEFAULT 0 ,
    budget BIGINT NULL ,
    revenue BIGINT NULL ,
    overview VARCHAR(8192) NULL ,
    backdrop_path VARCHAR(32) NULL ,
    poster_path VARCHAR(32) NULL ,
    hidden BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (director_id) REFERENCES movies.person(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS movies.movie_person
(
    movie_id INT NOT NULL ,
    person_id INT NOT NULL ,

    PRIMARY KEY (movie_id,person_id),
    FOREIGN KEY (movie_id) REFERENCES movies.movie(id) ON UPDATE CASCADE ON DELETE CASCADE ,
    FOREIGN KEY (person_id) REFERENCES movies.person(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS movies.movie_genre
(
    movie_id INT NOT NULL ,
    genre_id INT NOT NULL ,

    PRIMARY KEY (movie_id,genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies.movie(id) ON UPDATE CASCADE ON DELETE CASCADE ,
    FOREIGN KEY (genre_id) REFERENCES movies.genre(id) ON UPDATE CASCADE ON DELETE RESTRICT
);