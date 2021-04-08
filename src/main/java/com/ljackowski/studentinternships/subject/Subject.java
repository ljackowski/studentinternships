package com.ljackowski.studentinternships.subject;

import com.ljackowski.studentinternships.grade.Grade;

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
    @NotEmpty
    private String subjectName;

    @Column(name = "field_of_study")
    @NotEmpty
    private String fieldOfStudy;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Grade> gradeList;

    public Subject() {
    }

    public Subject(Long subjectId, @NotEmpty String subjectName, @NotEmpty String fieldOfStudy) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.fieldOfStudy = fieldOfStudy;
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
        this.fieldOfStudy = fieldOfStudy;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }
}