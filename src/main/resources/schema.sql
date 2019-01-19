DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;
DROP TABLE IF EXISTS genres;

CREATE TABLE authors(
    id BIGINT PRIMARY KEY AUTO_INCREMENT
    , first_name VARCHAR(50) NOT NULL
    , last_name VARCHAR(50) NOT NULL
);

CREATE TABLE genres(
    id BIGINT PRIMARY KEY AUTO_INCREMENT
    , genre VARCHAR(50) NOT NULL
);

CREATE TABLE books(
    id BIGINT PRIMARY KEY AUTO_INCREMENT
    , title VARCHAR(255)
    , author_id BIGINT
    , genre_id BIGINT
    , written VARCHAR(4)
    , FOREIGN KEY (author_id) REFERENCES authors(id)
    , FOREIGN KEY (genre_id) REFERENCES genres(id)
);

