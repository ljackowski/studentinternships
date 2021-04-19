package com.ljackowski.studentinternships.grade;

import com.ljackowski.studentinternships.intern.Intern;
import com.ljackowski.studentinternships.intern.InternService;
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
    private final InternService internService;

    @Autowired
    public GradeController(GradeService gradeService, SubjectService subjectService, StudentService studentService, InternService internService) {
        this.gradeService = gradeService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.internService = internService;
    }

    @RequestMapping("/list")
    public String getAllGrades(Model model) {
        model.addAttribute("grades", gradeService.getGrades());
        return "lists/gradesList";
    }

    @GetMapping("/delete")
    public String deleteGrade(@RequestParam("gradeId") long gradeId) {
        Student student = studentService.getStudentById(gradeService.getGradeById(gradeId).getStudent().getUserId());
        gradeService.deleteGradeById(gradeId);
        return "redirect:/students/student/" + student.getUserId();
    }

    @GetMapping("/edit/{gradeId}")
    public String editStudentGradeForm(@PathVariable("gradeId") long gradeId, Model model) {
        Grade grade = gradeService.getGradeById(gradeId);
        model.addAttribute("editGradeForm", grade);
        return "forms/editGradeForm";
    }

    @PostMapping("/edit/{gradeId}")
    public String editStudentGrade(@ModelAttribute Grade grade) {
        Student student = studentService.getStudentById(grade.getStudent().getUserId());
        Intern intern = internService.getInternByStudent(student);
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
        if (student.getRole().equals("STUDENT")){
            return "redirect:/students/student/" + student.getUserId();
        }
        else {
            return "redirect:/interns/intern/" + intern.getInternId();
        }

    }

//    @GetMapping("/editInternGrade/{gradeId}")
//    public String editInternGradeForm(@PathVariable("gradeId") long gradeId, Model model) {
//        Grade grade = gradeService.getGradeById(gradeId);
//        model.addAttribute("editGradeForm", grade);
//        return "forms/editGradeForm";
//    }
//
//    @PostMapping("/editInternGrade/{gradeId}")
//    public String editInternGrade(@ModelAttribute Grade grade) {
//        Student object = studentService.getStudentById(grade.getStudent().getUserId());
//        Intern inter = internService.getInternByStudent(object);
//        System.out.println(inter);
//        Subject subject = subjectService.getSubjectBuId(grade.getSubject().getSubjectId());
//        double averageGrade = 0;
//        if (object.getGradeList().size() != 0){
//            for (Grade grade1 : object.getGradeList()) {
//                if (grade1.getGradeId().equals(grade.getGradeId())){
//                    grade1.setGrade_number(grade.getGrade_number());
//                }
//                averageGrade += grade1.getGrade_number();
//            }
//            averageGrade /= object.getGradeList().size();
//        }
//        object.setAverageGrade(averageGrade);
//        studentService.updateStudent(object);
//        subjectService.updateSubject(subject);
//        gradeService.updateGrade(grade);
//        return "redirect:/interns/intern/" + inter.getInternId();
//    }




}

