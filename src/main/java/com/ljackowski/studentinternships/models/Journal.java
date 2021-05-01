package com.ljackowski.studentinternships.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "journals")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "entry_id")
    private Long entryId;

    @Column(name = "day")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    @Column(name = "starting_hour")
    private LocalTime startingTime;

    @Column(name = "ending_hour")
    private LocalTime endingTime;

    @Column(name = "hours")
    private int hours;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Journal() {
    }

    public Journal(Long entryId, LocalDate day, LocalTime startingTime, LocalTime endingTime, int hours, String description) {
        this.entryId = entryId;
        this.day = day;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.hours = hours;
        this.description = description;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate date) {
        this.day = date;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
