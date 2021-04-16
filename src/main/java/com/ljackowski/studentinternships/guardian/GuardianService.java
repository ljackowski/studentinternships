package com.ljackowski.studentinternships.guardian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuardianService {

    private final GuardianRepository guardianRepository;

    @Autowired
    public GuardianService(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    public List<Guardian> getAllGuardians(){
        return guardianRepository.findAll();
    }

    public Guardian getGuardianById(long guardianId){
        return guardianRepository.findById(guardianId).get();
    }

    public void addGuardian(Guardian guardian){
        guardianRepository.save(guardian);
    }

    public void deleteGuardianById(long guardianId){
        guardianRepository.deleteById(guardianId);
    }

    public void updateGuardian(Guardian guardian){
        guardianRepository.save(guardian);
    }
}
