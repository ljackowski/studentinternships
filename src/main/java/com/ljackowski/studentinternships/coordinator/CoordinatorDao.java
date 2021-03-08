package com.ljackowski.studentinternships.coordinator;


import java.util.List;
import java.util.Optional;

public interface CoordinatorDao {

    int insertCoordinator(Coordinator coordinator);

    List<Coordinator> selectAllCoordinators();

    Optional<Coordinator> selectCoordinatorById(String coordinatorId);

    int deleteCoordinatorById(int coordinatorId);

    int updateCoordinatorById(int coordinatorId, Coordinator coordinator);
}
