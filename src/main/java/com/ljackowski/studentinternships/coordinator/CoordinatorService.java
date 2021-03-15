package com.ljackowski.studentinternships.coordinator;

import com.ljackowski.studentinternships.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoordinatorService {
    private final CoordinatorRepository coordinatorRepository;

    @Autowired
    public CoordinatorService(CoordinatorRepository coordinatorRepository) {
        this.coordinatorRepository = coordinatorRepository;
    }

    public List<Coordinator> getCoordinators(){
        return coordinatorRepository.findAll();
    }

    public Coordinator getCoordinatorById(long coordinatorId){
        return coordinatorRepository.findById(coordinatorId).get();
    }

    public void addCoordinator(Coordinator coordinator){
        coordinatorRepository.save(coordinator);
    }

    public void updateCoordinator(Coordinator coordinator){
        coordinatorRepository.save(coordinator);
    }

    public void deleteCoordinatorById(long coordinatorId){
        coordinatorRepository.deleteById(coordinatorId);
    }

    public Coordinator getCoordinatorByFieldOfStudy(String fieldOfStudy){
        return coordinatorRepository.findByFieldOfStudy(fieldOfStudy);
    }

    public List<Student> getStudentsByFieldOfStudy(String fieldOfStudy){
        return coordinatorRepository.findAllByFieldOfStudy(fieldOfStudy);
    }

}
