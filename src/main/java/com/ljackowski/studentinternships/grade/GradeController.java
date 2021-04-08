package com.ljackowski.studentinternships.grade;

import com.ljackowski.studentinternships.student.Student;
import com.ljackowski.studentinternships.student.StudentService;
import com.ljackowski.studentinternships.subject.Subject;
import com.ljackowski.studentinternships.subject.SubjectService;
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

//    @GetMapping("/addGrade")
//    public String addGradeForm(Model model) {
//        model.addAttribute("addGradeForm", new Grade());
//        return "editGradeForm";
//    }
//
//    @PostMapping("/addGrade")
//    public String addGrade(@ModelAttribute Grade grade) {
//        gradeService.addGrade(grade);
//        return "redirect:/grades/list";
//    }

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

    @PostMapping("/edit/{gradeId}")
    public String editGrade(@ModelAttribute Grade grade) {
        Student student = studentService.getStudentById(grade.getStudent().getUserId());
        Subject subject = subjectService.getSubjectBuId(grade.getSubject().getSubjectId());
        double averageGrade = 0;
        if (student.getGradeList().size() != 0){
            for (Grade grade1 : student.getGradeList()) {
                if (grade1.getGradeId().equals(grade.getGradeId())){
                    grade1.setGrade_number(grade.getGrade_number());
                }
                averageGrade += grade1.getGrade_number();
            }
            averageGrade /= student.getGradeList().size();
        }
        student.setAverageGrade(averageGrade);
        studentService.updateStudent(student);
        subjectService.updateSubject(subject);
        gradeService.updateGrade(grade);
        return "redirect:/grades/list";
    }


}

