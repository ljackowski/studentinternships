package com.ljackowski.studentinternships.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "coordinators")
public class Coordinator extends User {

    @Column(name = "first_name")
    private String firstName;

    private String lastName;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "position")
    private String position;

    @OneToMany(mappedBy = "coordinator", cascade = CascadeType.PERSIST)
    private List<Student> students;


    public Coordinator() {
    }

    public Coordinator(String email, String password, String role, String firstName,
                       String lastName, String fieldOfStudy, String telephoneNumber, String position) {
        super(email, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.fieldOfStudy = fieldOfStudy;
        this.telephoneNumber = telephoneNumber;
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
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

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
