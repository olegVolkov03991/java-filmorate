MERGE INTO GENRES (GENRE_ID, NAME)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO mpa VALUES (1, 'G');
MERGE INTO mpa VALUES (2, 'PG');
MERGE INTO mpa VALUES (3, 'PG-13');
MERGE INTO mpa VALUES (4, 'R');
MERGE INTO mpa VALUES (5, 'NC-17');