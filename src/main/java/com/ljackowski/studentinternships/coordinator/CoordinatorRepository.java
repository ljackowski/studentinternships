package com.ljackowski.studentinternships.coordinator;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {
    Coordinator findByFieldOfStudy(String fieldOfStudy);
}
