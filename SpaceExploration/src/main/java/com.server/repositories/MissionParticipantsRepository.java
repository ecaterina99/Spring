package com.server.repositories;

import com.server.models.Mission;
import com.server.models.MissionParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionParticipantsRepository extends JpaRepository<MissionParticipants, Integer> {
    List<MissionParticipants> findByMissionId(int missionId);

    List<MissionParticipants> findByAstronautId(int astronautId);

}
