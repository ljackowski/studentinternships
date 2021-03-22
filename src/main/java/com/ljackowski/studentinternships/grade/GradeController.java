package com.ljackowski.studentinternships.grade;

import com.ljackowski.studentinternships.student.Student;
import com.ljackowski.studentinternships.student.StudentService;
import com.ljackowski.studentinternships.subject.SubjectService;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/grades")
public class GradeController {
    private final GradeService gradeService;
    private final SubjectService subjectService;
    private final StudentService studentService;

    @Autowired
    public GradeController(GradeService gradeService, SubjectService subjectService, StudentService studentService) {
        this.gradeService = gradeService;
        this.subjectService = subjectService;
        this.studentService = studentService;
    }

    @RequestMapping("/list")
    public String getAllGrades(Model model) {
        model.addAttribute("grades", gradeService.getGrades());
        return "gradesList";
    }

    @GetMapping("/addGrade")
    public String addGradeForm(Model model) {
        model.addAttribute("addGradeForm", new Grade());
        return "addGradeForm";
    }

    @PostMapping("/addGrade")
    public String addGrade(@ModelAttribute Grade grade) {
        gradeService.addGrade(grade);
        return "redirect:/grades/list";
    }

    @GetMapping("/delete")
    public String deleteGrade(@RequestParam("gradeId") long gradeId) {
        gradeService.deleteGradeById(gradeId);
        return "redirect:/grades/list";
    }

    @GetMapping("/edit/{gradeId}")
    public String editGradeForm(@PathVariable("gradeId") long gradeId, Model model) {
        Grade grade = gradeService.getGradeById(gradeId);
        model.addAttribute("editGradeForm", grade);
        return "editGradeForm";
    }

    //TODO sprawdzić jutro czy działa <3
    @PostMapping("/edit/{gradeId}")
    public String editGrade(@ModelAttribute Grade grade) {
        Student student = studentService.getStudentById(grade.getStudent().getUserId());
        double averageGrade = 0;
        if (student.getGradeList().size() != 0){
            for (Grade grade1 : student.getGradeList()) {
                averageGrade += grade1.getGrade();
            }
            averageGrade /= student.getGradeList().size();
        }
        student.setAverageGrade(averageGrade);
        studentService.updateStudent(student);
        gradeService.updateGrade(grade);
        return "redirect:/grades/list";
    }


}

