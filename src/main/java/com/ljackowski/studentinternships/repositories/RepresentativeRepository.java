package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Representative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentativeRepository extends JpaRepository<Representative,Long> {
}
