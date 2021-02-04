package com.ljackowski.studentinternships.employer;

import java.util.List;
import java.util.Optional;

public interface EmployerDao {

    int insertEmploer(Employer employer);

    List<Employer> selectAllEmployers();

    Optional<Employer> selectEmployerById(int employerId);

    int deleteEmployerById(int employerId);

    int updateEmployerById(int employerId, Employer employer);

}
