package com.ljackowski.studentinternships.controllers;

import com.ljackowski.studentinternships.models.*;
import com.ljackowski.studentinternships.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final StudentService studentService;
    private final CoordinatorService coordinatorService;
    private final GradeService gradeService;
    private final JournalService journalService;
    private final SubjectService subjectService;
    private final InternService internService;
    private final AddressService addressService;
    private final CompanyService companyService;
    private final RepresentativeService representativeService;
    private final GuardianService guardianService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AdminController(UserService userService, StudentService studentService, CoordinatorService coordinatorService, GradeService gradeService,
                           JournalService journalService, SubjectService subjectService,
                           InternService internService, AddressService addressService, CompanyService companyService,
                           RepresentativeService representativeService, GuardianService guardianService, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
        this.userService = userService;
        this.studentService = studentService;
        this.coordinatorService = coordinatorService;
        this.gradeService = gradeService;
        this.journalService = journalService;
        this.subjectService = subjectService;
        this.internService = internService;
        this.addressService = addressService;
        this.companyService = companyService;
        this.representativeService = representativeService;
        this.guardianService = guardianService;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
    }

    //region CRUD User
    @RequestMapping("/usersList")
    public String usersList(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "admin/usersList";
    }

    @GetMapping("/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/addUserForm";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/admin/usersList";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam(name = "userId") long userId) {
        userService.deleteUserById(userId);
        return "redirect:/admin/usersList";
    }

    @GetMapping("/editUser/{userId}")
    public String updateUserForm(@PathVariable(name = "userId") int userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("userToUpdate", user);
        return "admin/editUserForm";
    }

    @PostMapping("/editUser/{userId}")
    public String updateUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.updateUser(user);
        return "redirect:/admin/usersList";
    }
    //endregion

    //region CRUD Student

    @GetMapping("/student/{studentId}")
    public String getStudent(Model model, @PathVariable("studentId") long studentId) {
        Student student = studentService.getStudentById(studentId);
        student.setCompany(companyService.getCompanyById(733));
        model.addAttribute("student", student);
        return "admin/studentProfile";
    }

    @RequestMapping("/studentsList")
    public String getAllStudents(Model model) {
        List<Student> studentsList = studentService.getStudents();
        studentsList.removeIf(student -> student.getRole().equals("ROLE_INTERN"));
        for (Student student : studentsList) {
            student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
            studentService.updateStudent(student);
        }
        model.addAttribute("students", studentsList);
        return "admin/studentsList";
    }

    @GetMapping("/addStudent")
    public String addStudentForm(Model model) {
        model.addAttribute("newStudent", new Student());
        return "admin/addStudentForm";
    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute Student student) {
        student.setRole("ROLE_STUDENT".toUpperCase());
        student.setFieldOfStudy(student.getFieldOfStudy().toUpperCase());
        student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        String email = student.getEmail();
        addressService.addAddress(student.getAddress());
        studentService.addStudent(student);
        Student studentAfterCreation = studentService.getStudentByEmail(email);
        List<Subject> studentsSubjectList = subjectService.getAllSubjectsByFieldOfStudy(studentAfterCreation.getFieldOfStudy());
        for (Subject subject : studentsSubjectList) {
            studentAfterCreation.addGrade(gradeService.addGrade(new Grade(studentAfterCreation, subject, 0)));
        }
        return "redirect:/admin/studentsList";
    }

    @GetMapping("/deleteStudent")
    public String deleteStudentById(@RequestParam("userId") long userId) {
        studentService.deleteStudentById(userId);
        return "redirect:/admin/studentsList";
    }

    @GetMapping("/editStudent/{userId}")
    public String editStudentForm(@PathVariable(value = "userId") int userId, Model model) {
        Student student = studentService.getStudentById(userId);
        model.addAttribute("studentToEdit", student);
        return "admin/editStudentForm";
    }

    @PostMapping("/editStudent/{userId}")
    public String editStudent(@ModelAttribute Student student) {
        Student student1 = studentService.getStudentById(student.getUserId());
        student.setRole("object".toUpperCase());
        student.setFieldOfStudy(student.getFieldOfStudy().toUpperCase());
        student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
        student.setCompany(student1.getCompany());
        if (!student.getFieldOfStudy().equals(student1.getFieldOfStudy())) {
            List<Grade> gradeList = gradeService.getStudentsGrades(student.getUserId());
            for (Grade grade : gradeList) {
                gradeService.deleteGradeById(grade.getGradeId());
            }
            List<Subject> studentsSubjectList = subjectService.getAllSubjectsByFieldOfStudy(student.getFieldOfStudy());
            for (Subject subject : studentsSubjectList) {
                student.addGrade(gradeService.addGrade(new Grade(student, subject, 0)));
            }
        }
        addressService.updateAddress(student.getAddress());
        studentService.updateStudent(student);
        return "redirect:/admin/studentsList";
    }
    //endregion

    //region CRUD Intern
    public void SendInternshipNotification(Intern intern) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(intern.getStudent().getEmail());
        mailMessage.setFrom("pexy3475@gmail.com");
        mailMessage.setSubject("Informacja o stażu");
        if (!intern.isReserve()) {
            mailMessage.setText("Gratulujemy dostał się pan na staż na listę główną, proszę zalogować się do SOPiS przy pomocy tego emaila: "
                    + intern.getStudent().getEmail()
                    + "\n oraz tego hasła: "
                    + intern.getStudent().getPassword());
        } else {
            mailMessage.setText("Gratulujemy dostał się pan na staż na listę rezerwową. Poinformujemy Pana/Panią jeśli miejsce na liście głównej się zwolni");
        }
        javaMailSender.send(mailMessage);
    }

    @RequestMapping("/internsList")
    public String getAllInterns(Model model) {
        List<Intern> interns = internService.getAllInterns();
        model.addAttribute("interns", interns);
        return "admin/internsList";
    }

    @GetMapping("intern/{internId}")
    public String getIntern(@PathVariable("internId") long internId, Model model) {
        model.addAttribute("intern", internService.getInternById(internId));
        return "admin/internProfile";
    }

    @GetMapping("/addInterns")
    public String populateInternsTable() {
        List<Intern> internList = internService.getAllInterns();
        if (internList.isEmpty()) {
            int i = 1;
            List<Student> studentsQualified = studentService.getFirst20StudentsByAvgGrade();
            for (Student student : studentsQualified) {
                student.setRole("ROLE_INTERN");
                if (i <= 15) {
                    internService.addIntern(new Intern(student, null, false));
                } else {
                    internService.addIntern(new Intern(student, null, true));
                }
                i++;
            }
//            List<Intern> interns = internService.getAllInterns();
//            for (Intern intern : interns) {
//                SendInternshipNotification(intern);
//            }
        }
        return "redirect:/admin/internsList";
    }

    @GetMapping("/deleteIntern")
    public String deleteInternById(@RequestParam("internId") long internId) {
        Intern internToDelete = internService.getInternById(internId);
        if (!internToDelete.isReserve()) {
            List<Intern> interns = internService.getAllInterns();
            for (Intern intern : interns) {
                if (intern.isReserve()) {
                    intern.setReserve(false);
                    break;
                }
            }
        }
        internToDelete.getStudent().setRole("ROLE_STUDENT");
        internService.deleteIntern(internId);
        return "redirect:/admin/internsList";
    }

    @GetMapping("/deleteAllInterns")
    public String deleteAllInterns() {
        List<Intern> interns = internService.getAllInterns();
        for (Intern intern : interns) {
            intern.getStudent().setRole("ROLE_STUDENT");
        }
        internService.deleteAll();
        return "redirect:/admin/internsList";
    }

    @GetMapping("/editIntern/{internId}")
    public String editInternForm(@PathVariable("internId") long internId, Model model) {
        Intern intern = studentService.getStudentById(internId).getIntern();
        List<Company> companies = companyService.getFreeCompaniesInInternshipByFieldOfStudy(intern.getStudent().getFieldOfStudy(), true, 0);
        model.addAttribute("internToEdit", intern);
        model.addAttribute("companies", companies);
        return "admin/editInternForm";
    }

    @PostMapping("/editIntern/{internId}")
    public String editIntern(@ModelAttribute Intern intern) {
        Intern internBeforeUpdate = internService.getInternById(intern.getInternId());
        if (!internBeforeUpdate.isReserve()) {
            List<Intern> interns = internService.getAllInterns();
            Intern intern1 = internService.getInternById(interns.get(interns.indexOf(intern) + 1).getInternId());
            intern1.setReserve(false);
            SendInternshipNotification(intern1);
            internService.updateIntern(intern1);
        }
        internService.updateIntern(intern);
        return "redirect:/admin/internsList";
    }


    //endregion

    //region CRUD Intern Journal
    @GetMapping("/internJournal/{internId}")
    public String getInternJournalByInternId(@PathVariable(name = "internId") long internId, Model model) {
        model.addAttribute("journal", internService.getInternById(internId).getStudent().getJournal());
        return "admin/internshipJournal";
    }

    @GetMapping("/addInternEntry/{internId}")
    public String addInternEntryForm(Model model, @PathVariable(name = "internId") long internId) {
        Journal journal = new Journal();
        journal.setStudent(studentService.getStudentById(internService.getInternById(internId).getStudent().getUserId()));
        model.addAttribute("journal", journal);
        return "admin/addNewEntryToInternshipJournalForm";
    }

    @PostMapping("/addInternEntry/{internId}")
    public String addInternEntry(@ModelAttribute Journal journal, @PathVariable(name = "internId") long internId) {
        journal.setStudent(studentService.getStudentById(internService.getInternById(internId).getStudent().getUserId()));
        journalService.addEntry(journal);
        return "redirect:/admin/internJournal/" + internId;
    }

    @GetMapping("/deleteInternEntry")
    public String deleteInternEntryById(@RequestParam("entryId") long entryId) {
        long internId = journalService.getEntryById(entryId).getStudent().getIntern().getInternId();
        journalService.deleteEntryById(entryId);
        return "redirect:/admin/internJournal/" + internId;
    }

    @GetMapping("/editInternEntry/{entryId}")
    public String editInternEntryForm(@PathVariable("entryId") long entryId, Model model) {
        model.addAttribute("journal", journalService.getEntryById(entryId));
        return "admin/editInternEntryForm";
    }

    @PostMapping("/editInternEntry/{entryId}")
    public String editInternEntry(@ModelAttribute Journal journal) {
        journal.setStudent(journalService.getEntryById(journal.getEntryId()).getStudent());
        journalService.editEntry(journal);
        return "redirect:/admin/internJournal/" + journal.getStudent().getIntern().getInternId();
    }
    //endregion

    //region CRUD Coordinator
    @RequestMapping("/coordinatorsList")
    public String getAllCoordinators(Model model) {
        model.addAttribute("coordinators", coordinatorService.getCoordinators());
        return "admin/coordinatorsList";
    }

    @RequestMapping("/coordinator/{coordinatorId}")
    public String getCoordinatorById(Model model, @PathVariable("coordinatorId") long userId) {
        Coordinator coordinator = coordinatorService.getCoordinatorById(userId);
        List<Student> studentList = coordinator.getStudents();
        studentList.removeIf(student -> student.getRole().equals("ROLE_INTERN"));
        model.addAttribute("coordinator", coordinatorService.getCoordinatorById(userId));
        model.addAttribute("studentList", studentList);
        model.addAttribute("internList", internService.getInternsByCoordinator(coordinator));
        return "admin/coordinatorProfile";
    }

    @GetMapping("/addCoordinator")
    public String addCoordinatorForm(Model model) {
        model.addAttribute("newCoordinator", new Coordinator());
        return "admin/addCoordinatorForm";
    }

    @PostMapping("/addCoordinator")
    public String addCoordinator(@ModelAttribute Coordinator coordinator) {
        coordinator.setRole("ROLE_COORDINATOR");
        coordinator.setFieldOfStudy(coordinator.getFieldOfStudy().toUpperCase());
        coordinator.setPassword(passwordEncoder.encode(coordinator.getPassword()));
        coordinatorService.addCoordinator(coordinator);
        return "redirect:/admin/coordinatorsList";
    }

    @GetMapping("/deleteCoordinator")
    public String deleteCoordinatorById(@RequestParam("coordinatorId") long userId) {
        for (Student student : coordinatorService.getCoordinatorById(userId).getStudents()) {
            student.setCoordinator(null);
        }
        coordinatorService.deleteCoordinatorById(userId);
        return "redirect:/admin/coordinatorsList";
    }

    @GetMapping("/editCoordinator/{coordinatorId}")
    public String editCoordinatorForm(@PathVariable("coordinatorId") long userId, Model model) {
        model.addAttribute("coordinatorToEdit", coordinatorService.getCoordinatorById(userId));
        return "admin/editCoordinatorForm";
    }

    @PostMapping("/editCoordinator/{coordinatorId}")
    public String editCoordinator(@ModelAttribute Coordinator coordinator, @PathVariable("coordinatorId") long userId) {
        coordinator.setRole("ROLE_COORDINATOR".toUpperCase());
        coordinator.setFieldOfStudy(coordinator.getFieldOfStudy().toUpperCase());
        coordinatorService.updateCoordinator(coordinator);
        return "redirect:/admin/coordinatorsList";
    }
    //endregion

    //  region CRUD Student Journal
    @GetMapping("/studentJournal/{studentId}")
    public String getTraineeJournalByStudentId(@PathVariable(name = "studentId") long userId, Model model) {
        model.addAttribute("journal", studentService.getStudentById(userId).getJournal());
        return "admin/traineeJournal";
    }

    @GetMapping("/addStudentEntry/{studentId}")
    public String addEntryForm(Model model, @PathVariable(name = "studentId") long userId) {
        Journal journal = new Journal();
        journal.setStudent(studentService.getStudentById(userId));
        model.addAttribute("journal", journal);
        return "admin/addNewEntryToStudentJournalForm";
    }

    @PostMapping("/addStudentEntry/{userId}")
    public String addEntry(@ModelAttribute Journal journal, @PathVariable(name = "userId") long userId) {
        journal.setStudent(studentService.getStudentById(userId));
        journalService.addEntry(journal);
        return "redirect:/admin/studentJournal/" + userId;
    }

    @GetMapping("/deleteStudentEntry")
    public String deleteStudentEntryById(@RequestParam("entryId") long entryId) {
        long userId = journalService.getEntryById(entryId).getStudent().getUserId();
        journalService.deleteEntryById(entryId);
        return "redirect:/admin/studentJournal/" + userId;
    }

    @GetMapping("/editStudentEntry/{entryId}")
    public String editStudentEntryForm(@PathVariable("entryId") long entryId, Model model) {
        model.addAttribute("studentEntryToEdit", journalService.getEntryById(entryId));
        return "admin/editStudentEntryForm";
    }

    @PostMapping("/editStudentEntry/{entryId}")
    public String editStudentEntry(@ModelAttribute Journal journal) {
        long userId = journalService.getEntryById(journal.getEntryId()).getStudent().getUserId();
        journalService.editEntry(journal);
        return "redirect:/admin/studentJournal/" + userId;
    }
    //endregion

    //region CRUD Subjects
    @RequestMapping("/subjectsList")
    public String getAllSubjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "admin/subjectsList";
    }

    @GetMapping("/addSubject")
    public String addSubjectForm(Model model) {
        model.addAttribute("newSubject", new Subject());
        return "admin/addSubjectForm";
    }

    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute Subject subject) {
        subject.setFieldOfStudy(subject.getFieldOfStudy().toUpperCase());
        subjectService.addSubject(subject);
        List<Student> studentsList = studentService.getAllStudentsByFieldOfStudy(subject.getFieldOfStudy());
        for (Student student : studentsList) {
            student.addGrade(gradeService.addGrade(new Grade(student, subject, 0)));
            studentService.updateStudent(student);
        }
        return "redirect:/admin/subjectsList";
    }

    @GetMapping("/deleteSubject")
    public String deleteSubject(@RequestParam("subjectId") long subjectId) {
        subjectService.deleteSubjectById(subjectId);
        return "redirect:/admin/subjectsList";
    }

    @GetMapping("/editSubject/{subjectId}")
    public String editSubjectForm(@PathVariable("subjectId") long subjectId, Model model) {
        Subject subject = subjectService.getSubjectBuId(subjectId);
        model.addAttribute("subjectToEdit", subject);
        return "admin/editSubjectForm";
    }

    @PostMapping("/editSubject/{subjectId}")
    public String editSubject(@ModelAttribute Subject subject) {
        subject.setFieldOfStudy(subject.getFieldOfStudy().toUpperCase());
        List<Student> studentList = studentService.getAllStudentsByFieldOfStudy(subject.getFieldOfStudy());
        for (Student student : studentList) {
            if (!student.getFieldOfStudy().equals(subject.getFieldOfStudy())) {
                List<Grade> gradeList = gradeService.getStudentsGrades(student.getUserId());
                for (Grade grade : gradeList) {
                    gradeService.deleteGradeById(grade.getGradeId());
                }
                student.addGrade(gradeService.addGrade(new Grade(student, subject, 0)));
                studentService.updateStudent(student);
            } else {
                student.addGrade(gradeService.addGrade(new Grade(student, subject, 0)));
                studentService.updateStudent(student);
            }
        }
        subjectService.updateSubject(subject);
        return "redirect:/admin/subjectList";
    }
    //endregion

    //region CRUD Grades
    @GetMapping("/deleteGrade")
    public String deleteGrade(@RequestParam("gradeId") long gradeId) {
        Student student = studentService.getStudentById(gradeService.getGradeById(gradeId).getStudent().getUserId());
        gradeService.deleteGradeById(gradeId);
        if (student.getRole().equals("ROLE_STUDENT")) {
            return "redirect:/admin/student/" + student.getUserId();
        }
        if (student.getRole().equals("ROLE_INTERN")) {
            return "redirect:/admin/intern/" + internService.getInternByStudent(student).getInternId();
        }
        return "";
    }

    @GetMapping("/editGrade/{gradeId}")
    public String editStudentGradeForm(@PathVariable("gradeId") long gradeId, Model model) {
        Grade grade = gradeService.getGradeById(gradeId);
        model.addAttribute("gradeToEdit", grade);
        return "admin/editGradeForm";
    }

    @PostMapping("/editGrade/{gradeId}")
    public String editStudentGrade(@ModelAttribute Grade grade) {
        Student student = studentService.getStudentById(grade.getStudent().getUserId());
        Subject subject = subjectService.getSubjectBuId(grade.getSubject().getSubjectId());
        double averageGrade = 0;
        if (student.getGradeList().size() != 0) {
            for (Grade grade1 : student.getGradeList()) {
                if (grade1.getGradeId().equals(grade.getGradeId())) {
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
        if (student.getRole().equals("ROLE_STUDENT")) {
            return "redirect:/admin/student/" + student.getUserId();
        }
        if (student.getRole().equals("ROLE_INTERN")) {
            return "redirect:/admin/intern/" + internService.getInternByStudent(student).getInternId();
        }
        return "";
    }
    //endregion

    //region CRUD Company
    @RequestMapping("/companiesList")
    public String getAllCompanies(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        for (Company company : companies) {
            companyService.updateCompany(company);
        }
        model.addAttribute("companies", companies);
        return "admin/companiesList";
    }

    @RequestMapping("/company/{companyId}")
    public String getCompanyById(Model model, @PathVariable("companyId") long companyId) {
        Company company = companyService.getCompanyById(companyId);
        if (company.isPartOfInternship()) {
            company.getStudentList().removeIf(student -> student.getRole().equals("ROLE_STUDENT"));
        } else {
            company.getStudentList().removeIf(student -> student.getRole().equals("ROLE_INTERN"));
        }
        model.addAttribute("company", company);
        return "admin/companyProfile";
    }

    @GetMapping("/addCompany")
    public String addCompanyForm(Model model) {
        model.addAttribute("newCompany", new Company());
        return "admin/addCompanyForm";
    }

    @PostMapping("/addCompany")
    public String addCompanyToInternShip(@ModelAttribute Company company) {
        company.setFieldOfStudy(company.getFieldOfStudy().toUpperCase());
        representativeService.addRepresentative(company.getRepresentative());
        addressService.addAddress(company.getAddress());
        companyService.addCompany(company);
        return "redirect:/admin/companiesList";
    }

    @GetMapping("/{companyId}/addGuardianToCompany")
    public String addGuardianToCompanyForm(@PathVariable("companyId") long companyId, Model model) {
        Guardian guardian = new Guardian();
        guardian.setCompany(companyService.getCompanyById(companyId));
        model.addAttribute("newGuardian", guardian);
        return "admin/addGuardianToCompany";
    }

    @PostMapping("/{companyId}/addGuardianToCompany")
    public String addGuardianToCompany(@PathVariable("companyId") long companyId, @ModelAttribute Guardian guardian) {
        guardianService.addGuardian(guardian);
        return "redirect:/admin/company/" + companyId;
    }

    @GetMapping("/deleteGuardian")
    public String deleteCompanyGuardian(@RequestParam("guardianId") long guardianId) {
        long companyId = guardianService.getGuardianById(guardianId).getCompany().getCompanyId();
        guardianService.deleteGuardianById(guardianId);
        return "redirect:/admin/company/" + companyId;
    }

    @GetMapping("/editGuardian/{guardianId}")
    public String editGuardianForm( @PathVariable("guardianId") long guardianId, Model model) {
        model.addAttribute("guardianToEdit", guardianService.getGuardianById(guardianId));
        return "admin/editGuardianForm";
    }

    @PostMapping("/editGuardian/{guardianId}")
    public String editGuardian(@PathVariable("guardianId") long guardianId,@ModelAttribute Guardian guardian){
        guardianService.updateGuardian(guardian);
        return "redirect:/admin/company/" + guardian.getCompany().getCompanyId();
    }


    @GetMapping("/deleteCompany")
    public String deleteCompanyById(@RequestParam("companyId") long companyId) {
        for (Student student : studentService.getAllStudentsInCompany(companyService.getCompanyById(companyId))) {
            student.setCompany(null);
        }
        companyService.deleteById(companyId);
        return "redirect:/admin/companiesList";
    }

    @GetMapping("/editCompany/{companyId}")
    public String editCompanyForm(@PathVariable("companyId") long companyId, Model model) {
        Company company = companyService.getCompanyById(companyId);
        model.addAttribute("companyToEdit", company);
        return "admin/editCompanyForm";
    }

    @PostMapping("/editCompany/{companyId}")
    public String editCompany(@ModelAttribute Company company) {
        addressService.updateAddress(company.getAddress());
        representativeService.updateRepresentative(company.getRepresentative());
        companyService.updateCompany(company);
        return "redirect:/admin/companiesList";
    }
    //endregion

}
