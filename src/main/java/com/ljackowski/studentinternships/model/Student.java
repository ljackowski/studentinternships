package com.ljackowski.studentinternships.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Student {
    private final UUID studentId;
    private final String imie, nazwisko, email, nrTelefonu, nrIndeksu, kierunekStudiow, stopien;
    private final double sredniaOcen;

    public Student(@JsonProperty("studentId") UUID studentId, @JsonProperty("imie") String imie, @JsonProperty("nazwisko") String nazwisko,
                   @JsonProperty("email") String email, @JsonProperty("nrTelefonu") String nrTelefonu, @JsonProperty("nrIndeksu") String nrIndeksu,
                   @JsonProperty("kierunekStudiow") String kierunekStudiow, @JsonProperty("stopien") String stopien,
                   @JsonProperty("sredniaOcen") double sredniaOcen) {
        this.studentId = studentId;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.nrTelefonu = nrTelefonu;
        this.nrIndeksu = nrIndeksu;
        this.kierunekStudiow = kierunekStudiow;
        this.stopien = stopien;
        this.sredniaOcen = sredniaOcen;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public String getNrTelefonu() {
        return nrTelefonu;
    }

    public String getNrIndeksu() {
        return nrIndeksu;
    }

    public String getKierunekStudiow() {
        return kierunekStudiow;
    }

    public String getStopien() {
        return stopien;
    }

    public double getSredniaOcen() {
        return sredniaOcen;
    }
}

//{
//        "imie": "Jan",
//        "nazwisko": "Kowalskii",
//        "email": "jkowalski@gmail.com",
//        "nrTelefonu": "666555444",
//        "nrIndeksu": "75823",
//        "kierunekStudiow": "Informatyka",
//        "stopien": "I",
//        "sredniaOcen": 4.2
//        }