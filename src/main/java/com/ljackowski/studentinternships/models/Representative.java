package com.ljackowski.studentinternships.models;

import com.ljackowski.studentinternships.models.Company;

import javax.persistence.*;

@Entity
@Table(name = "representatives")
public class Representative {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "representative_id")
    private Long representativeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "position")
    private String position;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @OneToOne(mappedBy = "representative")
    private Company company;

    public Representative() {
    }

    public Representative(String firstName, String lastName, String email, String position, String telephoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.telephoneNumber = telephoneNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(Long representativeId) {
        this.representativeId = representativeId;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
