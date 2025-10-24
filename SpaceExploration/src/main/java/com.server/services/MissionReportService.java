package com.server.services;

import com.server.dto.MissionParticipantsDTO;
import com.server.dto.MissionReportDTO;
import com.server.models.Mission;
import com.server.models.MissionReport;
import com.server.repositories.MissionReportRepository;
import com.server.repositories.MissionRepository;
import com.server.util.MissionResult;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MissionReportService {
    private final MissionReportRepository missionReportRepository;
    private final ModelMapper modelMapper;
    private final BudgetService budgetService;
    private final MissionRepository missionRepository;

    public MissionReportService(MissionReportRepository missionReportRepository, BudgetService budgetService, MissionRepository missionRepository) {
        this.missionReportRepository = missionReportRepository;
        this.budgetService = budgetService;
        this.missionRepository = missionRepository;
        this.modelMapper = new ModelMapper();
    }

    @Transactional(readOnly = true)
    public MissionReportDTO getMissionReportById(int id) {
        MissionReport missionReport = missionReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mission Report with id: " + id + " not found"));
        return MissionReportDTO.fromMissionReport(missionReport);

    }

    @Transactional(readOnly = true)
    public List<MissionReportDTO> getAllMissionReports() {
        List<MissionReport> missionReports = missionReportRepository.findAll();
        return missionReports.stream()
                .map(missionReport -> modelMapper.map(missionReport, MissionReportDTO.class))
                .toList();
    }

    @Transactional
    public MissionReportDTO createReport(Integer missionId, MissionResult missionResult, List<MissionParticipantsDTO> participants) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission with id: " + missionId + " not found"));

        MissionReport report = new MissionReport();
        report.setMission(mission);
        report.setSuccessful(missionResult.isSuccess());
        missionReportRepository.save(report);

        System.out.println("=== Creating Mission Report ===");
        System.out.println("Mission ID: " + missionId);
        System.out.println("Is Successful: " + missionResult.isSuccess());
        System.out.println("Success Chance: " + missionResult.getSuccessChance());
        System.out.println("Issues: " + missionResult.getIssues());
        System.out.println("Alien Attack: " + missionResult.isAlienAttack());
        System.out.println("===============================");
        int totalSalary = budgetService.calculateMissionExpenses(missionId, participants);

        MissionReportDTO dto = MissionReportDTO.builder()
                .id(report.getId())
                .isSuccessful(missionResult.isSuccess())
                .missionId(mission.getId())
                .missionName(mission.getName())
                .destinationName(mission.getDestination().getDestinationName())
                .difficultyLevel(mission.getDifficultyLevel())
                .paymentAmount(mission.getPaymentAmount())
                .crewSize(mission.getCrewSize())
                .successChance(missionResult.getSuccessChance())
                .issues(missionResult.getIssues())
                .alienAttack(missionResult.isAlienAttack())
                .crewSizeDeficit(missionResult.getCrewSizeDeficit())
                .missingSpecializations(missionResult.getMissingSpecializations())
                .notReadyAstronauts(missionResult.getNotReadyAstronauts())
                .totalSalary(totalSalary)
                .build();

        System.out.println("=== Mission Report DTO ===");
        System.out.println("DTO isSuccessful: " + dto.isSuccessful());
        System.out.println("DTO successChance: " + dto.getSuccessChance());
        System.out.println("DTO issues size: " + (dto.getIssues() != null ? dto.getIssues().size() : "null"));
        System.out.println("==========================");

        return dto;
    }
}

