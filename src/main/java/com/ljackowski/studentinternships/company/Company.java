package com.ljackowski.studentinternships.company;


import com.ljackowski.studentinternships.representative.Representative;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "companies")
public class Company{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "company_name")
    @NotEmpty
    private String companyName;

    @Column(name = "city")
    @NotEmpty
    private String city;

    @Column(name = "street")
    @NotEmpty
    private String street;

    @Column(name = "building_number")
    @NotEmpty
    private String buildingNumber;

    @Column(name = "zip_code")
    @NotEmpty
    private String zipCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "representative_id", referencedColumnName = "representative_id")
    private Representative representative;

    public Company() {
    }

    public Company(Long companyId, @NotEmpty String companyName, @NotEmpty String city,
                   @NotEmpty String street, @NotEmpty String buildingNumber, @NotEmpty String zipCode,
                   Representative representative) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.zipCode = zipCode;
        this.representative = representative;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }
}
