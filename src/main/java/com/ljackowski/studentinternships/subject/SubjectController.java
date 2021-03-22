package com.ljackowski.studentinternships.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping("/list")
    public String getAllSubjects(Model model){
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "subjectsList";
    }

    @GetMapping("/addSubject")
    public String addSubjectForm(Model model){
        model.addAttribute("addSubjectForm", new Subject());
        return "addSubjectForm";
    }

    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute Subject subject){
        subject.setFieldOfStudy(subject.getFieldOfStudy().toUpperCase());
        subjectService.addSubject(subject);
        return "redirect:/subjects/list";
    }

    @GetMapping("/delete")
    public String deleteSubject(@RequestParam("subjectId") long subjectId){
        subjectService.deleteSubjectById(subjectId);
        return "redirect:/subjects/list";
    }

    @GetMapping("/edit/{subjectId}")
    public String editSubjectForm(@PathVariable("subjectId") long subjectId, Model model){
        Subject subject =  subjectService.getSubjectBuId(subjectId);
        model.addAttribute("editSubjectForm", subject);
        return "editSubjectForm";
    }

    @PostMapping("/edit/{subjectId}")
    public String editSubject(@ModelAttribute Subject subject){
        subject.setFieldOfStudy(subject.getFieldOfStudy().toUpperCase());
        subjectService.updateSubject(subject);
        return "redirect:/subjects/list";
    }



}
