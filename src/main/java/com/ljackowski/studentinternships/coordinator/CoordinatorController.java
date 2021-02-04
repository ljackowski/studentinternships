package com.ljackowski.studentinternships.coordinator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import java.util.List;

@Controller
public class CoordinatorController {

    private final CoordinatorService coordinatorService;
    @Autowired
    ServletContext servletContext;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @RequestMapping("/coordinators")
    public String getAllCoordinators(Model model){
        List<Coordinator> coordinatorList = coordinatorService.getAllCoordinators();
        model.addAttribute("coordinators", coordinatorList);
        return "coordinators";
    }

}
