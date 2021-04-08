package com.ljackowski.studentinternships.company;


import com.ljackowski.studentinternships.address.Address;
import com.ljackowski.studentinternships.intern.Intern;
import com.ljackowski.studentinternships.representative.Representative;
import com.ljackowski.studentinternships.student.Student;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "is_part_of_internship")
    private boolean isPartOfInternship;

    @Column(name = "free_spaces")
    private int freeSpaces;

    @OneToMany(mappedBy = "company")
    private List<Intern> internList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "representative_id")
    private Representative representative;

    @OneToOne(mappedBy = "company")
    private Student student;

    public Company() {
    }

    public Company(String companyName, boolean isPartOfInternship, int freeSpaces, Address address, Representative representative) {
        this.companyName = companyName;
        this.isPartOfInternship = isPartOfInternship;
        this.freeSpaces = freeSpaces;
        this.address = address;
        this.representative = representative;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isPartOfInternship() {
        return isPartOfInternship;
    }

    public void setPartOfInternship(boolean partOfInternship) {
        isPartOfInternship = partOfInternship;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public int getFreeSpaces() {
        return freeSpaces;
    }

    public void setFreeSpaces(int freeSpaces) {
        this.freeSpaces = freeSpaces;
    }

    public List<Intern> getInternList() {
        return internList;
    }

    public void setInternList(List<Intern> internList) {
        this.internList = internList;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }
}
