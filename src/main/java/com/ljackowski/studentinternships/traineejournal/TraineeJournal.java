package com.ljackowski.studentinternships.traineejournal;

import com.ljackowski.studentinternships.student.Student;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "traineejournal")
public class TraineeJournal {

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
    private double hours;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public TraineeJournal() {
    }

    public TraineeJournal(Long entryId, LocalDate day, LocalTime startingTime, LocalTime endingTime, double hours, String description) {
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

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
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

    public void addStudentToEntry(Student student) {
        this.student = student;
    }
}
