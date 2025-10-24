package com.server.repositories;

import com.server.models.MissionParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionParticipantsRepository extends JpaRepository<MissionParticipants, Integer> {
    List<MissionParticipants> findByMissionId(int missionId);
    boolean existsByMissionIdAndAstronautId(int missionId, int astronautId);
}
