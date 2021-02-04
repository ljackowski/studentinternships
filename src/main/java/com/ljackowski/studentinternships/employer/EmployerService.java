package com.ljackowski.studentinternships.employer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("employerService")
public class EmployerService {
    private final EmployerDao employerDao;

    public EmployerService(@Qualifier("EmployerDao") EmployerDao employerDao) {
        this.employerDao = employerDao;
    }

    public int addEmployer(Employer employer){
        return employerDao.insertEmploer(employer);
    }

    public List<Employer> getAllEmployers(){
        return employerDao.selectAllEmployers();
    }

    public Optional<Employer> getEmployerById(int employerId){
        return employerDao.selectEmployerById((employerId));
    }

    public int deleteEmployer(int employerId){
        return  employerDao.deleteEmployerById(employerId);
    }

    public int updateEmployer(int employerId, Employer newEmployer){
        return employerDao.updateEmployerById(employerId, newEmployer);
    }

}
