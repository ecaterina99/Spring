package com.server.repositories;

import com.server.models.MissionSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionSpecializationRepository extends JpaRepository<MissionSpecialization, Integer> {
    List<MissionSpecialization> findByMissionId(int missionId);

    @Query("SELECT mrs FROM mission_specializations mrs WHERE mrs.mission.id = :missionId")
    List<MissionSpecialization> findByMissionIdWithQuery(@Param("missionId") int missionId);

    void deleteByMissionId(int missionId);

    boolean existsByMissionIdAndSpecialization(int missionId, MissionSpecialization.Specialization specialization);

    Optional<MissionSpecialization> findByMissionIdAndSpecialization(int missionId, MissionSpecialization.Specialization specialization);
}