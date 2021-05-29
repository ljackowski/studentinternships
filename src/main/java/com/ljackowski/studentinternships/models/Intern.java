package com.ljackowski.studentinternships.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "interns")
public class Intern{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "intern_id")
    private Long internId;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "reserve")
    private boolean reserve;

    public Intern() {
    }

    public Intern(Student student, Company company ,boolean reserve) {
        this.company = company;
        this.student = student;
        this.reserve = reserve;
    }

    public Long getInternId() {
        return internId;
    }

    public void setInternId(Long internId) {
        this.internId = internId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isReserve() {
        return reserve;
    }

    public void setReserve(boolean reserve) {
        this.reserve = reserve;
    }
}
