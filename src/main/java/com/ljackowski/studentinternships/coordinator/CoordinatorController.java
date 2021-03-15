package com.ljackowski.studentinternships.coordinator;

import com.ljackowski.studentinternships.student.Student;
import com.ljackowski.studentinternships.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.List;

@Controller
@RequestMapping("/coordinators")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;
    private final StudentService studentService;
    @Autowired
    ServletContext servletContext;

    public CoordinatorController(CoordinatorService coordinatorService, StudentService studentService) {
        this.coordinatorService = coordinatorService;
        this.studentService = studentService;
    }

    @GetMapping("/addCoordinator")
    public String addCoordinatorForm(Model model) {
        model.addAttribute("addCoordinatorForm", new Coordinator());
        return "addCoordinatorForm";
    }

    @PostMapping("/addCoordinator")
    public String addCoordinator(@ModelAttribute Coordinator coordinator) {
        coordinator.setRole("coordinator".toUpperCase());
        coordinator.setFieldOfStudy(coordinator.getFieldOfStudy().toUpperCase());
        coordinatorService.addCoordinator(coordinator);
        return "redirect:/coordinators/list";
    }

    @RequestMapping("/list")
    public String getAllCoordinators(Model model) {
        List<Coordinator> coordinatorList = coordinatorService.getCoordinators();
        studentService.getStudents();
        model.addAttribute("coordinators", coordinatorList);
        return "coordinatorsList";
    }

    @RequestMapping("/coordinator/{userId}")
    public Coordinator getCoordinatorById(@PathVariable("userId") long userId) {
        return coordinatorService.getCoordinatorById(userId);
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
        return "editCoordinatorForm";
    }

    @PostMapping("/edit/{userId}")
    public String editCoordinator(@ModelAttribute Coordinator coordinator) {
        coordinator.setRole("coordinator".toUpperCase());
        coordinator.setFieldOfStudy(coordinator.getFieldOfStudy().toUpperCase());
        coordinatorService.updateCoordinator(coordinator);
        return "redirect:/coordinators/list";
    }


}
