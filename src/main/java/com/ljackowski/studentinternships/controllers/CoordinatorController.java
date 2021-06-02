package com.ljackowski.studentinternships.controllers;

import com.ljackowski.studentinternships.models.Company;
import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.Student;
import com.ljackowski.studentinternships.services.*;
import com.ljackowski.studentinternships.models.InternshipPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public final CompanyService companyService;

    @Autowired
    public CoordinatorController(CoordinatorService coordinatorService, InternService internService, InternshipPlanService internshipPlanService, CompanyService companyService) {
        this.coordinatorService = coordinatorService;
        this.internService = internService;
        this.internshipPlanService = internshipPlanService;
        this.companyService = companyService;
    }

    @GetMapping("/{coordinatorId}")
    @PreAuthorize("authentication.principal.userId == #coordinatorId")
    public String getCoordinator(@PathVariable("coordinatorId") long coordinatorId, Model model) {
        model.addAttribute("coordinator", coordinatorService.getCoordinatorById(coordinatorId));
        return "coordinator/coordinatorProfile";
    }

    //region Student
    @GetMapping("/{coordinatorId}/studentsList")
    @PreAuthorize("authentication.principal.userId == #coordinatorId")
    public String getCoordinatorsStudents(@PathVariable("coordinatorId") long coordinatorId, Model model) {
        List<Student> studentList = coordinatorService.getCoordinatorById(coordinatorId).getStudents();
        studentList.removeIf(student -> student.getRole().equals("ROLE_INTERN"));
        model.addAttribute("students", studentList);
        return "coordinator/studentsList";
    }

    @GetMapping("/{coordinatorId}/student/{studentId}")
    @PreAuthorize("authentication.principal.userId == #coordinatorId and authentication.principal.fieldOfStudy == #student.fieldOfStudy and #student.role == 'ROLE_STUDENT'")
    public String getCoordinatorsStudent(@PathVariable("coordinatorId") long coordinatorId, Model model, @PathVariable("studentId") Student student) {
        model.addAttribute("student", student);
        return "coordinator/studentProfile";
    }

    @GetMapping("/{coordinatorId}/traineeJournal/{studentId}")
    @PreAuthorize("authentication.principal.userId == #coordinatorId and authentication.principal.fieldOfStudy == #student.fieldOfStudy and #student.role == 'ROLE_STUDENT'")
    public String getCoordinatorsStudentsTraineeJournal(@PathVariable("coordinatorId") long coordinatorId, Model model, @PathVariable("studentId") Student student) {
        model.addAttribute("student", student);
        return "coordinator/traineeJournal";
    }
    //endregion

    //region Companies
    @GetMapping("/{coordinatorId}/companiesList")
    @PreAuthorize("authentication.principal.userId == #coordinatorId")
    public String getCompanies(@PathVariable("coordinatorId") long coordinatorId, Model model) {
        model.addAttribute("companies", companyService.getCompaniesInInternshipByFieldOfStudy(coordinatorService.getCoordinatorById(coordinatorId).getFieldOfStudy(), true));
        return "coordinator/internshipCompanies";
    }

    @GetMapping("/{coordinatorId}/company/{companyId}")
    @PreAuthorize("(authentication.principal.userId == #coordinatorId) and (authentication.principal.fieldOfStudy == #company.fieldOfStudy)")
    public String getCompanyProfile(@PathVariable("coordinatorId") long coordinatorId, @PathVariable("companyId") Company company,
                                    Model model ) {
        model.addAttribute("company", company);
        return "coordinator/companyProfile";
    }


    //region InternshipPlan

    @GetMapping("/{coordinatorId}/addEntry/{companyId}")
    @PreAuthorize("(authentication.principal.userId == #coordinatorId) and (authentication.principal.fieldOfStudy == #company.fieldOfStudy)")
    public String addInternProgramEntryForm(@PathVariable("coordinatorId") long coordinatorId, @PathVariable("companyId") Company company, Model model) {
        InternshipPlan internshipPlan = new InternshipPlan();
        internshipPlan.setCompany(company);
        model.addAttribute("entry", internshipPlan);
        return "coordinator/addInternshipPlanEntryForm";
    }

    @PostMapping("/{coordinatorId}/addEntry/{companyId}")
    @PreAuthorize("(authentication.principal.userId == #coordinatorId) and (authentication.principal.fieldOfStudy == #company.fieldOfStudy)")
    public String addInternProgramEntry(@PathVariable("coordinatorId") long coordinatorId, @ModelAttribute InternshipPlan internshipPlan, @PathVariable("companyId") Company company) {
        internshipPlan.setCompany(company);
        internshipPlanService.addPlanEntry(internshipPlan);
        return "redirect:/coordinator/" + coordinatorId + "/company/" + company.getCompanyId();
    }

    @GetMapping("/{coordinatorId}/deleteEntry/{companyId}")
    @PreAuthorize("(authentication.principal.userId == #coordinatorId) and (authentication.principal.fieldOfStudy == #company.fieldOfStudy)")
    public String deletePlanEntry(@PathVariable("coordinatorId") long coordinatorId, @PathVariable("companyId") Company company, @RequestParam("entryId") long entryId) {
        internshipPlanService.deleteEntryById(entryId);
        return "redirect:/coordinator/" + coordinatorId + "/company/" + company.getCompanyId();
    }

    @GetMapping("/{coordinatorId}/editEntry/{companyId}/{entryId}")
    @PreAuthorize("(authentication.principal.userId == #coordinatorId) and (authentication.principal.fieldOfStudy == #company.fieldOfStudy)")
    public String editInternPlanEntryForm(@PathVariable("coordinatorId") long coordinatorId, @PathVariable("companyId") Company company, Model model, @PathVariable("entryId") long entryId) {
        model.addAttribute("entry", internshipPlanService.getPlanEntryById(entryId));
        return "coordinator/editInternshipPlanEntryForm";
    }

    @PostMapping("/{coordinatorId}/editEntry/{companyId}/{entryId}")
    @PreAuthorize("(authentication.principal.userId == #coordinatorId) and (authentication.principal.fieldOfStudy == #company.fieldOfStudy)")
    public String editInternPlanEntry(@PathVariable("coordinatorId") long coordinatorId, @PathVariable("companyId") Company company, @ModelAttribute InternshipPlan internshipPlan, @PathVariable("entryId") long entryId) {
        internshipPlan.setInternshipPlanId(entryId);
        internshipPlan.setCompany(company);
        internshipPlanService.updateEntry(internshipPlan);
        return "redirect:/coordinator/" + coordinatorId + "/company/" + company.getCompanyId();
    }
    //endregion

    //endregion

    //region Intern
    @GetMapping("/{coordinatorId}/internsList")
    @PreAuthorize("authentication.principal.userId == #coordinatorId")
    public String getCoordinatorsInterns(@PathVariable("coordinatorId") long coordinatorId, Model model) {
        model.addAttribute("interns", internService.getInternsByCoordinator(coordinatorService.getCoordinatorById(coordinatorId)));
        return "coordinator/internsList";
    }

    @GetMapping("/{coordinatorId}/intern/{internId}")
    @PreAuthorize("authentication.principal.userId == #coordinatorId and authentication.principal.fieldOfStudy == #intern.student.fieldOfStudy")
    public String getCoordinatorsIntern(@PathVariable("coordinatorId") long coordinatorId, Model model, @PathVariable("internId") Intern intern) {
        model.addAttribute("intern", intern);
        return "coordinator/internProfile";
    }

    @GetMapping("/{coordinatorId}/internshipJournal/{internId}")
    @PreAuthorize("authentication.principal.userId == #coordinatorId and authentication.principal.fieldOfStudy == #intern.student.fieldOfStudy")
    public String getCoordinatorsStudentsInternshipJournal(@PathVariable("coordinatorId") long coordinatorId, Model model, @PathVariable("internId") Intern intern) {
        model.addAttribute("intern", intern);
        return "coordinator/internshipJournal";
    }

    //endregion

}
