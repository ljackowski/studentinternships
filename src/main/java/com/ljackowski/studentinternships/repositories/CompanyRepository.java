package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAllByPartOfInternshipAndFreeSpacesGreaterThan(boolean partOfInternship, int freeSpaces);
    List<Company> findAllByFieldOfStudyAndPartOfInternship(String fieldOfStudy, boolean partOfInternship);
    List<Company> findAllByFieldOfStudyAndPartOfInternshipAndFreeSpacesGreaterThan(String fieldOfStudy, boolean partOfInternship, int freeSpaces);
}
