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

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @Column(name = "starting_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startingDate;

    @Column(name = "ending_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endingDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "representative_id")
    private Representative representative;

    @OneToMany(mappedBy = "company")
    private List<Student> studentList;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Guardian> guardianList;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<InternshipPlan> internshipPlan;

    public Company() {
    }

    public Company(String companyName, boolean PartOfInternship, int freeSpaces, LocalDate startingDate, LocalDate endingDate, Address address, Representative representative, String fieldOfStudy) {
        this.companyName = companyName;
        this.partOfInternship = PartOfInternship;
        this.freeSpaces = freeSpaces;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.address = address;
        this.representative = representative;
        this.fieldOfStudy = fieldOfStudy;
    }

    public List<Guardian> getGuardianList() {
        return guardianList;
    }

    public void setGuardianList(List<Guardian> guardianList) {
        this.guardianList = guardianList;
    }

    public void addGuardianToList(Guardian guardian){
        this.guardianList.add(guardian);
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
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

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public List<InternshipPlan> getInternshipPlan() {
        return internshipPlan;
    }

    public void setInternshipPlan(List<InternshipPlan> internshipPlan) {
        this.internshipPlan = internshipPlan;
    }

    public void addGuardian(Guardian guardian){
        this.guardianList.add(guardian);
    }
}
