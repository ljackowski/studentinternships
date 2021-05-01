package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
