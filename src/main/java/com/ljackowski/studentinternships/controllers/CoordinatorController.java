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
@RequestMapping("/coordinators")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;
    private final StudentService studentService;
    private final InternService internService;
    private final InternshipPlanService internshipPlanService;

    @Autowired
    public CoordinatorController(CoordinatorService coordinatorService, StudentService studentService, InternService internService, InternshipPlanService internshipPlanService) {
        this.coordinatorService = coordinatorService;
        this.studentService = studentService;
        this.internService = internService;
        this.internshipPlanService = internshipPlanService;
    }

    @RequestMapping("/list")
    public String getAllCoordinators(Model model) {
        List<Coordinator> coordinatorList = coordinatorService.getCoordinators();
        studentService.getStudents();
        model.addAttribute("coordinators", coordinatorList);
        return "lists/coordinatorsList";
    }

    @RequestMapping("/coordinator/{userId}")
    public String getCoordinatorById(Model model, @PathVariable("userId") long userId){
        model.addAttribute("coordinator", coordinatorService.getCoordinatorById(userId));
        return "profiles/coordinatorProfile";
    }

    @GetMapping("/addCoordinator")
    public String addCoordinatorForm(Model model) {
        model.addAttribute("addCoordinatorForm", new Coordinator());
        return "forms/addCoordinatorForm";
    }

    @PostMapping("/addCoordinator")
    public String addCoordinator(@ModelAttribute Coordinator coordinator) {
        coordinator.setRole("coordinator".toUpperCase());
        coordinator.setFieldOfStudy(coordinator.getFieldOfStudy().toUpperCase());
        coordinatorService.addCoordinator(coordinator);
        return "redirect:/coordinators/list";
    }

    @GetMapping("/delete")
    public String deleteCoordinatorById(@RequestParam("userId") long userId) {
        for (Student student : coordinatorService.getCoordinatorById(userId).getStudents()) {
            student.setCoordinator(null);
        }
        coordinatorService.deleteCoordinatorById(userId);
        return "redirect:/coordinators/list";
    }

    @GetMapping("/edit/{userId}")
    public String editCoordinatorForm(@PathVariable("userId") long userId, Model model) {
        Coordinator coordinator = coordinatorService.getCoordinatorById(userId);
        model.addAttribute("editCoordinatorForm", coordinator);
        return "forms/editCoordinatorForm";
    }

    @PostMapping("/edit/{userId}")
    public String editCoordinator(@ModelAttribute Coordinator coordinator) {
        coordinator.setRole("coordinator".toUpperCase());
        coordinator.setFieldOfStudy(coordinator.getFieldOfStudy().toUpperCase());
        coordinatorService.updateCoordinator(coordinator);
        return "redirect:/coordinators/list";
    }

//    Internship Plan

    @RequestMapping("/internProgram/{internId}")
    public String getInternProgram(@PathVariable("internId") long internId, Model model){
        model.addAttribute("plan", internshipPlanService.getInternPlan(internService.getInternById(internId)));
        return "lists/internPlan";
    }

    @GetMapping("/addProgramEntry/{internId}")
    public String addInternProgramEntryForm(@PathVariable("internId") long internId, Model model){
        InternshipPlan internshipPlan = new InternshipPlan();
        Intern intern = internService.getInternById(internId);
        internshipPlan.setIntern(intern);
        internshipPlan.setCoordinator(intern.getStudent().getCoordinator());
        model.addAttribute("plan");
        return "";
    }

    @PostMapping("/addProgramEntry/{internId}")
    public String addInternProgramEntry(@ModelAttribute InternshipPlan internshipPlan){
        internshipPlanService.addPlanEntry(internshipPlan);
        return "";
    }

    @GetMapping("/deleteProgramEntry")
    public String deletePlanEntry(@RequestParam("entryId") long entryId){
        internshipPlanService.deleteEntryById(entryId);
        return "";
    }

    @GetMapping("/editProgramEntry/{entryId}")
    public String editInternPlanEntryForm(Model model, @PathVariable("entryId") long entryId){
        model.addAttribute("entry", internshipPlanService.getPlanEntryById(entryId));
        return "";
    }

    @PostMapping("/editProgramEntry/{entryId}")
    public String editInternPlanEntry(@ModelAttribute InternshipPlan internshipPlan){
        internshipPlanService.updateEntry(internshipPlan);
        return "";
    }


}
