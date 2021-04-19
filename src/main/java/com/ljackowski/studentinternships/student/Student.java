package com.ljackowski.studentinternships.student;

import com.ljackowski.studentinternships.address.Address;
import com.ljackowski.studentinternships.company.Company;
import com.ljackowski.studentinternships.coordinator.Coordinator;
import com.ljackowski.studentinternships.grade.Grade;
import com.ljackowski.studentinternships.intern.Intern;
import com.ljackowski.studentinternships.journal.Journal;
import com.ljackowski.studentinternships.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student extends User {

    @Column(name = "student_index")
    private int studentIndex;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @Column(name = "degree")
    private String degree;

    @Column(name = "average_grade")
    private double averageGrade;

    @ManyToOne
    private Coordinator coordinator;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "student")
    private Intern intern;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Grade> gradeList = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Journal> journal;

    public Student(String email, String password, String role, int studentIndex,
                   String firstName, String lastName, String telephoneNumber,
                   String fieldOfStudy, String degree, double averageGrade, Address address) {
        super(email, password, role);
        this.studentIndex = studentIndex;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.fieldOfStudy = fieldOfStudy;
        this.degree = degree;
        this.averageGrade = averageGrade;
        this.address = address;
    }

    public Student() {
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void addGrade(Grade grade) {
        this.gradeList.add(grade);
    }

    public List<Journal> getTraineeJournal() {
        return journal;
    }

    public void setTraineeJournal(List<Journal> journal) {
        this.journal = journal;
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

    public Intern getIntern() {
        return intern;
    }

    public void setIntern(Intern intern) {
        this.intern = intern;
    }
}
