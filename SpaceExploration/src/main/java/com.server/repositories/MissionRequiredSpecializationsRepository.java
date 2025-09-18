package com.server.repositories;

import com.server.models.MissionRequiredSpecializations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRequiredSpecializationsRepository extends JpaRepository<MissionRequiredSpecializations, Integer> {
    List<MissionRequiredSpecializations> findByMissionId(int missionId);

    @Query("SELECT mrs FROM mission_required_specializations mrs WHERE mrs.mission.id = :missionId")
    List<MissionRequiredSpecializations> findByMissionIdWithQuery(@Param("missionId") int missionId);

    void deleteByMissionId(int missionId);

    boolean existsByMissionIdAndSpecialization(int missionId, MissionRequiredSpecializations.Specialization specialization);

    Optional<MissionRequiredSpecializations> findByMissionIdAndSpecialization(int missionId, MissionRequiredSpecializations.Specialization specialization);
}