package com.ljackowski.studentinternships.coordinator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import java.util.List;

@Controller
@RequestMapping("/coordinators")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;
    @Autowired
    ServletContext servletContext;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @GetMapping("/addCoordinator")
    public String addCoordinatorForm(Model model){
        model.addAttribute("addCoordinatorForm", new Coordinator());
        return "addCoordinatorForm";
    }

    @PostMapping("/addCoordinator")
    public String addCoordinator(@ModelAttribute Coordinator coordinator){
        coordinator.setRole("coordinator");
        coordinatorService.addCoordinator(coordinator);
        return "redirect:/coordinators/list";
    }

    @RequestMapping("/list")
    public String getAllCoordinators(Model model){
        List<Coordinator> coordinatorList = coordinatorService.getCoordinators();
        model.addAttribute("coordinators", coordinatorList);
        return "coordinatorsList";
    }


}
