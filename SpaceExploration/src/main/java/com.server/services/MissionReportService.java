package com.server.services;

import com.server.dto.MissionDTO;
import com.server.dto.MissionReportDTO;
import com.server.models.Mission;
import com.server.models.MissionReport;
import com.server.repositories.MissionParticipantsRepository;
import com.server.repositories.MissionReportRepository;
import com.server.repositories.MissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MissionReportService {
    private final MissionReportRepository missionReportRepository;
    private final ModelMapper modelMapper;
    private final MissionService missionService;
    private final MissionRepository missionRepository;

    public MissionReportService(MissionReportRepository missionReportRepository, MissionService missionService, MissionRepository missionRepository) {
        this.missionReportRepository = missionReportRepository;
        this.missionService = missionService;
        this.missionRepository = missionRepository;
        this.modelMapper = new ModelMapper();
    }
    @Transactional(readOnly = true)
    public MissionReportDTO getMissionReportById(int id) {
     MissionReport missionReport = missionReportRepository.findById(id)
             .orElseThrow(()-> new EntityNotFoundException("Mission Report with id: " + id + " not found"));
        return MissionReportDTO.fromMissionReport(missionReport);

    }
    @Transactional(readOnly = true)
    public List<MissionReportDTO> getAllMissionReports() {
        List<MissionReport> missionReports = missionReportRepository.findAll();
        return missionReports.stream()
                .map(missionReport -> modelMapper.map(missionReport, MissionReportDTO.class))
                .toList();
    }

    public MissionReportDTO createReport(Integer missionId, boolean success) {
        MissionReport report = new MissionReport();
        Optional<Mission> mission = missionRepository.findById(missionId);
        report.setMission(mission.get());
        report.setSuccessful(success);
        missionReportRepository.save(report);
        return modelMapper.map(report, MissionReportDTO.class);
    }
}

