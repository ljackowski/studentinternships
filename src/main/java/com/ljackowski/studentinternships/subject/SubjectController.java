package com.ljackowski.studentinternships.subject;

import com.ljackowski.studentinternships.grade.Grade;
import com.ljackowski.studentinternships.grade.GradeService;
import com.ljackowski.studentinternships.student.Student;
import com.ljackowski.studentinternships.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final GradeService gradeService;

    @Autowired
    public SubjectController(SubjectService subjectService, StudentService studentService, GradeService gradeService) {
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.gradeService = gradeService;
    }

    @RequestMapping("/list")
    public String getAllSubjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "subjectsList";
    }

    @GetMapping("/addSubject")
    public String addSubjectForm(Model model) {
        model.addAttribute("addSubjectForm", new Subject());
        return "addSubjectForm";
    }

    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute Subject subject) {
        subject.setFieldOfStudy(subject.getFieldOfStudy().toUpperCase());
        subjectService.addSubject(subject);
        List<Student> studentsList = studentService.getAllStudentsByFieldOfStudy(subject.getFieldOfStudy());
        for (Student student : studentsList) {
            student.addGrade(gradeService.addGrade(new Grade(student, subject, 0)));
            studentService.updateStudent(student);
        }
        return "redirect:/subjects/list";
    }

    @GetMapping("/delete")
    public String deleteSubject(@RequestParam("subjectId") long subjectId) {
        subjectService.deleteSubjectById(subjectId);
        return "redirect:/subjects/list";
    }

    @GetMapping("/edit/{subjectId}")
    public String editSubjectForm(@PathVariable("subjectId") long subjectId, Model model) {
        Subject subject = subjectService.getSubjectBuId(subjectId);
        model.addAttribute("editSubjectForm", subject);
        return "editSubjectForm";
    }

    @PostMapping("/edit/{subjectId}")
    public String editSubject(@ModelAttribute Subject subject) {
        subject.setFieldOfStudy(subject.getFieldOfStudy().toUpperCase());
        List<Student> studentList = studentService.getAllStudentsByFieldOfStudy(subject.getFieldOfStudy());
        for (Student student : studentList) {
            if (!student.getFieldOfStudy().equals(subject.getFieldOfStudy())) {
                List<Grade> gradeList = gradeService.getStudentsGrades(student.getUserId());
                for (Grade grade : gradeList) {
                    gradeService.deleteGradeById(grade.getGradeId());
                }
                student.addGrade(gradeService.addGrade(new Grade(student, subject, 0)));
                studentService.updateStudent(student);
            } else {
                student.addGrade(gradeService.addGrade(new Grade(student, subject, 0)));
                studentService.updateStudent(student);
            }
        }
        subjectService.updateSubject(subject);

        return "redirect:/subjects/list";
    }
}
