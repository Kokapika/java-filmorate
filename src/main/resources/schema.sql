DROP TABLE IF EXISTS users, friends, mpa_ratings, films, genres, film_genres, film_likes CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id     INT        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email       VARCHAR    NOT NULL UNIQUE,
    login       VARCHAR    NOT NULL UNIQUE,
    name        VARCHAR,
    birthday    DATE       NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa_ratings
(
    mpa_rating_id    INT       PRIMARY KEY UNIQUE,
    mpa_name         VARCHAR   NOT NULL
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id      INT        PRIMARY KEY UNIQUE,
    genre_name    VARCHAR    NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    film_id          INT             GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name             VARCHAR         NOT NULL,
    description      VARCHAR(200)    NOT NULL,
    release_date     DATE            NOT NULL,
    duration         INT             NOT NULL,
    mpa_rating_id    INT             REFERENCES mpa_ratings(mpa_rating_id),
    rate             INT
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id      INT    REFERENCES users(user_id),
    friend_id    INT    REFERENCES users(user_id),
    PRIMARY KEY(user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id     INT    REFERENCES films(film_id),
    genre_id    INT    REFERENCES genres(genre_id),
    PRIMARY KEY(film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id    INT    REFERENCES films(film_id),
    user_id    INT    REFERENCES users(user_id),
    PRIMARY KEY(film_id, user_id)
);

