package com.ljackowski.studentinternships.coordinator;

import com.ljackowski.studentinternships.student.Student;
import com.ljackowski.studentinternships.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "coordinators")
public class Coordinator extends User {

    @Column(name = "first_name")
    @NotEmpty
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    private String lastName;

    @Column(name = "field_of_study")
    @NotEmpty
    private String fieldOfStudy;

    @Column(name = "telephone_number")
    @NotEmpty
    private String telephoneNumber;

    @OneToMany(mappedBy = "coordinator", cascade = CascadeType.PERSIST)
    private List<Student> students;

    public Coordinator() {
    }

    public Coordinator(String email, String password, String role, String firstName,
                       String lastName, String fieldOfStudy, String telephoneNumber) {
        super(email, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.fieldOfStudy = fieldOfStudy;
        this.telephoneNumber = telephoneNumber;
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
