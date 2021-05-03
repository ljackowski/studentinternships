package com.ljackowski.studentinternships.models;

import javax.persistence.*;

@Entity
@Table(name = "internship_plan")
public class InternshipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "internship_plan_id")
    private Long internshipPlanId;

    @ManyToOne
    @JoinColumn(name = "intern_id")
    private Intern intern;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "description")
    private String description;

    public InternshipPlan() {
    }

    public InternshipPlan(Intern intern, Company company, String description) {
        this.intern = intern;
        this.company = company;
        this.description = description;
    }

    public Long getInternshipPlanId() {
        return internshipPlanId;
    }

    public void setInternshipPlanId(Long internshipPlanId) {
        this.internshipPlanId = internshipPlanId;
    }

    public Intern getIntern() {
        return intern;
    }

    public void setIntern(Intern intern) {
        this.intern = intern;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
