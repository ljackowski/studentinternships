package com.ljackowski.studentinternships.coordinator;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("coordinatorService")
public class CoordinatorService {
    private final CoordinatorDao coordinatorDao;

    @Autowired
    public CoordinatorService(@Qualifier("CoordinatorDao") CoordinatorDao coordinatorDao){
        this.coordinatorDao = coordinatorDao;
    }

    public  int addCoordinator(Coordinator coordinator){
        return coordinatorDao.insertCoordinator(coordinator);
    }

    public List<Coordinator> getAllCoordinators(){
        return coordinatorDao.selectAllCoordinators();
    }

    public int deleteCoordinator(int coordinatorId){
        return coordinatorDao.deleteCoordinatorById(coordinatorId);
    }

    public int updateCoordinator(int coordinatorId, Coordinator newCoordinator){
        return coordinatorDao.updateCoordinatorById(coordinatorId, newCoordinator);
    }

}
