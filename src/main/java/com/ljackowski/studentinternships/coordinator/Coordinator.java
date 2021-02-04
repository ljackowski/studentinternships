package com.ljackowski.studentinternships.coordinator;

import java.util.UUID;

public class Coordinator {
    private int coordinatorId, userId, telephoneNumber;
    private String firstName, lastName, email, fieldOfStudy;

    public Coordinator() {
    }

    public Coordinator(int coordinatorId, int userId, int telephoneNumber, String firstName, String lastName, String email, String fieldOfStudy) {
        this.coordinatorId = coordinatorId;
        this.userId = userId;
        this.telephoneNumber = telephoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.fieldOfStudy = fieldOfStudy;
    }

    public int getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(int coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(int telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
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

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }
}
