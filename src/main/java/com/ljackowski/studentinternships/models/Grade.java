package com.ljackowski.studentinternships.models;

import javax.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "grade_id")
    private Long gradeId;

    @Column(name = "grade", columnDefinition = "Numeric(3,2)")
    private double gradeNumber;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne()
    @JoinColumn(name = "subject_id")
    private Subject subject;


    public Grade() {
    }

    public Grade(Student student, Subject subject) {
        this.student = student;
        this.subject = subject;
    }

    public Grade(Student student, Subject subject, double gradeNumber) {
        this.student = student;
        this.subject = subject;
        this.gradeNumber = gradeNumber;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public double getGradeNumber() {
        return gradeNumber;
    }

    public void setGradeNumber(double grade) {
        this.gradeNumber = grade;
    }
}
