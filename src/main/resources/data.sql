MERGE INTO GENRES (GENRE_ID, NAME)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO MPA VALUES (1, 'G');
MERGE INTO MPA VALUES (2, 'PG');
MERGE INTO MPA VALUES (3, 'PG-13');
MERGE INTO MPA VALUES (4, 'R');
MERGE INTO MPA VALUES (5, 'NC-17');