package com.server.repositories;

import com.server.models.MissionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MissionReportRepository extends JpaRepository<MissionReport, Integer> {
    Optional<MissionReport> getMissionReportByMission_Id(int missionId);
}
