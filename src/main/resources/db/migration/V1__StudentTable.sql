CREATE TABLE student(
    id UUID NOT NULL PRIMARY KEY,
    imie VARCHAR(100) NOT NULL,
    nazwisko VARCHAR(100) NOT NULL,
    email VARCHAR(200) UNIQUE NOT NULL,
    nrTelefonu NUMERIC NOT NULL,
    nrIndeksu NUMERIC NOT NULL,
    kierunekStudiow VARCHAR(300) NOT NULL,
    stopien VARCHAR(1) NOT NULL,
    sredniaOcen DECIMAL NOT NULL
);