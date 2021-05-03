package com.ljackowski.studentinternships.controllers;

import com.ljackowski.studentinternships.services.CoordinatorService;
import com.ljackowski.studentinternships.models.Coordinator;
import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.services.InternService;
import com.ljackowski.studentinternships.models.InternshipPlan;
import com.ljackowski.studentinternships.services.InternshipPlanService;
import com.ljackowski.studentinternships.models.Student;
import com.ljackowski.studentinternships.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/coordinator")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;
    private final InternService internService;
    private final InternshipPlanService internshipPlanService;

    @Autowired
    public CoordinatorController(CoordinatorService coordinatorService, InternService internService, InternshipPlanService internshipPlanService) {
        this.coordinatorService = coordinatorService;
        this.internService = internService;
        this.internshipPlanService = internshipPlanService;
    }

    @GetMapping("/{coordinatorId}")
    public String getCoordinatorsStudents(@PathVariable("coordinatorId") long coordinatorId, Model model) {
        model.addAttribute("coordinator", coordinatorService.getCoordinatorById(coordinatorId));
        return "profiles/coordinatorProfile";
    }

//    Internship Plan

    @RequestMapping("/internProgram/{internId}")
    public String getInternProgram(@PathVariable("internId") long internId, Model model) {
        model.addAttribute("plan", internshipPlanService.getInternPlan(internService.getInternById(internId)));
        return "lists/internPlan";
    }

    @GetMapping("/addProgramEntry/{internId}")
    public String addInternProgramEntryForm(@PathVariable("internId") long internId, Model model) {
        InternshipPlan internshipPlan = new InternshipPlan();
        Intern intern = internService.getInternById(internId);
        internshipPlan.setIntern(intern);
        model.addAttribute("plan");
        return "";
    }

    @PostMapping("/addProgramEntry/{internId}")
    public String addInternProgramEntry(@ModelAttribute InternshipPlan internshipPlan) {
        internshipPlanService.addPlanEntry(internshipPlan);
        return "";
    }

    @GetMapping("/deleteProgramEntry")
    public String deletePlanEntry(@RequestParam("entryId") long entryId) {
        internshipPlanService.deleteEntryById(entryId);
        return "";
    }

    @GetMapping("/editProgramEntry/{entryId}")
    public String editInternPlanEntryForm(Model model, @PathVariable("entryId") long entryId) {
        model.addAttribute("entry", internshipPlanService.getPlanEntryById(entryId));
        return "";
    }

    @PostMapping("/editProgramEntry/{entryId}")
    public String editInternPlanEntry(@ModelAttribute InternshipPlan internshipPlan) {
        internshipPlanService.updateEntry(internshipPlan);
        return "";
    }

}
