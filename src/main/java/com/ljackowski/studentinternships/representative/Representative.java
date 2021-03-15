package com.ljackowski.studentinternships.representative;

import com.ljackowski.studentinternships.company.Company;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "representatives")
public class Representative{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "representative_id")
    private Long representativeId;

    @Column(name = "telephone_number")
    @NotEmpty
    private String telephoneNumber;

    @Column(name = "first_name")
    @NotEmpty
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    private String lastName;

    @Column(name = "email")
    @NotEmpty
    private String email;

    @Column(name = "company_name")
    @NotEmpty
    private String companyName;

    @OneToOne(mappedBy = "representative", cascade = CascadeType.ALL)
    private Company company;

    public Representative() {
    }

    public Representative(Long representativeId, @NotEmpty String telephoneNumber, @NotEmpty String firstName, @NotEmpty String lastName,
                          @NotEmpty String email, @NotEmpty String companyName, Company company) {
        this.representativeId = representativeId;
        this.telephoneNumber = telephoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.companyName = companyName;
        this.company = company;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(Long representativeId) {
        this.representativeId = representativeId;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
