INSERT INTO authors(first_name, last_name) VALUES ('Lewis', 'Carrol');
INSERT INTO authors(first_name, last_name) VALUES ('Charlotte', 'Bronte');
INSERT INTO authors(first_name, last_name) VALUES ('Miguel', 'de Cervantes');
INSERT INTO authors(first_name, last_name) VALUES ('Herbert', 'Wells');
INSERT INTO authors(first_name, last_name) VALUES ('Leo', 'Tolstoy');

INSERT INTO genres(genre) VALUES ('Literary realism');
INSERT INTO genres(genre) VALUES ('Fantasy');
INSERT INTO genres(genre) VALUES ('Autobiography');
INSERT INTO genres(genre) VALUES ('Novel');
INSERT INTO genres(genre) VALUES ('Science-fiction');

INSERT INTO books(title, author_id, genre_id, written) VALUES ('Alice in Wonderland', 1, 2, '1865');
INSERT INTO books(title, author_id, genre_id, written) VALUES ('Jane Eyre', 2, 3, '1847');
INSERT INTO books(title, author_id, genre_id, written) VALUES ('Don Quixote', 3, 4, '1615');
INSERT INTO books(title, author_id, genre_id, written) VALUES ('The Time Machine', 4, 5, '1895');
INSERT INTO books(title, author_id, genre_id, written) VALUES ('Anna Karenina', 5, 1, '1878');
