package com.ljackowski.studentinternships.models;

import com.ljackowski.studentinternships.models.Coordinator;
import com.ljackowski.studentinternships.models.Intern;

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
    @JoinColumn(name = "coordinator_id")
    private Coordinator coordinator;

    @Column(name = "description")
    private String description;

    public InternshipPlan() {
    }

    public InternshipPlan(Intern intern, Coordinator coordinator, String description) {
        this.intern = intern;
        this.coordinator = coordinator;
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

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
