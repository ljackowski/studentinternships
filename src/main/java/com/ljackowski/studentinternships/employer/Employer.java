package com.ljackowski.studentinternships.employer;

public class Employer {

    private int employerId, userId, telephoneNumber, companyId;
    private String firstName, lastName, email;

    public Employer() {
    }

    public Employer(int employerId, int userId, int telephoneNumber, int companyId, String firstName, String lastName, String email) {
        this.employerId = employerId;
        this.userId = userId;
        this.telephoneNumber = telephoneNumber;
        this.companyId = companyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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
}
