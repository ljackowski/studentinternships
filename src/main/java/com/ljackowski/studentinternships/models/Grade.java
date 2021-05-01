package com.ljackowski.studentinternships.models;

import javax.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "grade_id")
    private Long gradeId;

    @Column(name = "grade")
    private double grade_number;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne()
    @JoinColumn(name = "subject_id")
    private Subject subject;


    public Grade() {
    }

    public Grade(Student student, Subject subject, double grade_number) {
        this.student = student;
        this.subject = subject;
        this.grade_number = grade_number;
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

    public double getGrade_number() {
        return grade_number;
    }

    public void setGrade_number(double grade) {
        this.grade_number = grade;
    }
}
