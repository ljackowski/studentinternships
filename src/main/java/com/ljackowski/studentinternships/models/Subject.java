package com.ljackowski.studentinternships.models;

import com.ljackowski.studentinternships.models.Grade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Grade> gradeList;

    public Subject() {
    }

    public Subject(String subjectName, String fieldOfStudy) {
        this.subjectName = subjectName;
        this.fieldOfStudy = fieldOfStudy.toUpperCase();
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy.toUpperCase();
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }
}
