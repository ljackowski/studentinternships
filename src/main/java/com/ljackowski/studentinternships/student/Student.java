package com.ljackowski.studentinternships.student;

import com.ljackowski.studentinternships.coordinator.Coordinator;
import com.ljackowski.studentinternships.grade.Grade;
import com.ljackowski.studentinternships.traineejournal.TraineeJournal;
import com.ljackowski.studentinternships.user.User;
import com.sun.istack.Nullable;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student extends User {

    @Column(name = "student_index")
    @NotNull
    private int studentIndex;

    @Column(name = "first_name")
    @NotEmpty
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    private String lastName;

    @Column(name = "telephone_number")
    @NotEmpty
    private String telephoneNumber;

    @Column(name = "field_of_study")
    @NotEmpty
    private String fieldOfStudy;

    @Column(name = "degree")
    @NotEmpty
    private String degree;

    @Column(name = "average_grade")
    private double averageGrade;

    @ManyToOne
    @Nullable
    private Coordinator coordinator;

    @OneToMany(mappedBy = "student")
    private List<Grade> gradeList = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<TraineeJournal> traineeJournal;



    public Student() {
    }

    public Student(String email, String password, String role, @NotNull int studentIndex,
                   @NotEmpty String firstName, @NotEmpty String lastName, @NotEmpty String telephoneNumber,
                   @NotEmpty String fieldOfStudy, @NotEmpty String degree, double averageGrade, Coordinator coordinator) {
        super(email, password, role);
        this.studentIndex = studentIndex;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.fieldOfStudy = fieldOfStudy;
        this.degree = degree;
        this.averageGrade = averageGrade;
        this.coordinator = coordinator;
    }

    public void addGrade(Grade grade){
        this.gradeList.add(grade);
    }

    public List<TraineeJournal> getTraineeJournal() {
        return traineeJournal;
    }

    public void setTraineeJournal(List<TraineeJournal> traineeJournal) {
        this.traineeJournal = traineeJournal;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public int getStudentIndex() {
        return studentIndex;
    }

    public void setStudentIndex(int studentIndex) {
        this.studentIndex = studentIndex;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }
}
