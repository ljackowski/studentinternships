package com.ljackowski.studentinternships.models;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
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
    private boolean partOfInternship;

    @Column(name = "free_spaces")
    private int freeSpaces;

    @Column(name = "starting_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startingDate;

    @Column(name = "ending_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endingDate;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "guardian_id")
    private Guardian guardian;

    @OneToMany(mappedBy = "company")
    private List<InternshipPlan> internshipPlan;

    public Company() {
    }

    public Company(String companyName, boolean PartOfInternship, int freeSpaces, LocalDate startingDate, LocalDate endingDate, Address address, Representative representative, Guardian guardian) {
        this.companyName = companyName;
        this.partOfInternship = PartOfInternship;
        this.freeSpaces = freeSpaces;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.address = address;
        this.representative = representative;
        this.guardian = guardian;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isPartOfInternship() {
        return partOfInternship;
    }

    public void setPartOfInternship(boolean partOfInternship) {
        this.partOfInternship = partOfInternship;
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
