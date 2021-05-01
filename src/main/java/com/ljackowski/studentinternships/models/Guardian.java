package com.ljackowski.studentinternships.models;

import javax.persistence.*;

@Entity
@Table(name = "guardians")
public class Guardian {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "guardian_id")
    private Long guardianId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "position")
    private String position;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "guardian")
    private Company company;

    public Guardian() {
    }

    public Guardian(String firstName, String lastName, String telephoneNumber, String email, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.position = position;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(Long guardianId) {
        this.guardianId = guardianId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
