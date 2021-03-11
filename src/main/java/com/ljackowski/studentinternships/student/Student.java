package com.ljackowski.studentinternships.student;

import com.ljackowski.studentinternships.user.User;

public class Student extends User {

    private String firstName, lastName, email, telephoneNumber, fieldOfStudy, degree;
    private int userId, studentIndex, coordinatorId, employerId;
    private double averageGrade;

    public Student(){
    }

    public Student(int userId, String firstName, String lastName, String email, String telephoneNumber, int studentIndex,
                   String fieldOfStudy, String degree, int coordinatorId, int employerId, double averageGrade) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.studentIndex = studentIndex;
        this.fieldOfStudy = fieldOfStudy;
        this.degree = degree;
        this.coordinatorId = coordinatorId;
        this.employerId = employerId;
        this.averageGrade = averageGrade;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStudentIndex() {
        return studentIndex;
    }

    public void setStudentIndex(int studentIndex) {
        this.studentIndex = studentIndex;
    }

    public int getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(int coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
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