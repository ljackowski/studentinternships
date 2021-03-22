package com.ljackowski.studentinternships.traineejournal;

import com.ljackowski.studentinternships.student.Student;
import com.ljackowski.studentinternships.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TraineeJournalController {

    private final TraineeJournalService traineeJournalService;
    private final StudentService studentService;

    @Autowired
    public TraineeJournalController(TraineeJournalService traineeJournalService, StudentService studentService) {
        this.traineeJournalService = traineeJournalService;
        this.studentService = studentService;
    }

    @RequestMapping("/list")
    public String getAllTraineeJournals(Model model){
        model.addAttribute("traineeJournals", traineeJournalService.getAllTraineeJournals());
        return "traineeJournalsList";
    }

//    @GetMapping("/addStudentEntry/{studentId}")
//    public String addEntryForm(Model model, @PathVariable(name = "studentId") long studentId){
//        Student student = studentService.getStudentById(studentId);
//        TraineeJournal traineeJournal = new TraineeJournal();
//        traineeJournal.setStudent(student);
//        model.addAttribute("addNewEntryToTraineeJournalForm", traineeJournal);
//        return "addNewEntryToTraineeJournalForm";
//    }
//
//    @PostMapping("/addStudentEntry/{studentId}")
//    public String addEntry(@ModelAttribute TraineeJournal traineeJournal, @PathVariable(name = "studentId") long studentId){
//        Student student = studentService.getStudentById(studentId);
//        traineeJournal.addStudentToEntry(student);
//        traineeJournalService.addEntry(traineeJournal);
//        return "redirect:/list";
//    }
//
//    @GetMapping("/delete")
//    public String deleteStudentEntryById(@RequestParam("entryId") long entryId){
//        traineeJournalService.deleteEntryById(entryId);
//        return "redirect:/list";
//    }
//
//    @GetMapping("/editStudentEntry/{entryId}")
//    public String editStudentEntryForm(@PathVariable("entryId") long entryId, Model model){
//        TraineeJournal traineeJournal = traineeJournalService.getEntryById(entryId);
//        model.addAttribute("editStudentEntryForm", traineeJournal);
//        return "editStudentEntryForm";
//    }
//
//    @PostMapping("/editStudentEntry/{entryId}")
//    public String editStudentEntry(@ModelAttribute TraineeJournal traineeJournal){
//        traineeJournalService.editEntry(traineeJournal);
//        return "redirect:/list";
//    }



}
