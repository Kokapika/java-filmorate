DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS film_likes CASCADE;

CREATE TABLE IF NOT EXISTS users (
    user_id     INT        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email       VARCHAR    NOT NULL UNIQUE,
    login       VARCHAR    NOT NULL,
    name        VARCHAR    NOT NULL,
    birthday    DATE       NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
    mpa_rating_id    INT        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name      VARCHAR    NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id      INT        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name    VARCHAR    NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id         INT             GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name            VARCHAR         NOT NULL,
    description     VARCHAR(200)    NOT NULL,
    release_date    DATE            NOT NULL,
    duration        INT             NOT NULL,
    mpa_rating_id   INT             NOT NULL REFERENCES mpa_ratings(mpa_rating_id)
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id     INT    NOT NULL REFERENCES films(film_id),
    genre_id    INT    NOT NULL REFERENCES genres(genre_id)
);

CREATE TABLE IF NOT EXISTS friendships (
    from_user_id    INT        NOT NULL REFERENCES users(user_id),
    to_user_id      INT        NOT NULL REFERENCES users(user_id),
    isAgree         BOOLEAN    NOT NULL,
    PRIMARY KEY (from_user_id, to_user_id)
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id    INT    NOT NULL REFERENCES films(film_id),
    user_id    INT    NOT NULL REFERENCES users(user_id)
);