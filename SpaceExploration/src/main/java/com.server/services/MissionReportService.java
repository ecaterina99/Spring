package com.server.services;

import com.server.dto.MissionReportDTO;
import com.server.models.MissionReport;
import com.server.repositories.MissionParticipantsRepository;
import com.server.repositories.MissionReportRepository;
import com.server.repositories.MissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MissionReportService {
    private final MissionReportRepository missionReportRepository;
    private final ModelMapper modelMapper;
    private final MissionRepository missionRepository;
    private final MissionParticipantsRepository missionParticipantsRepository;


    public MissionReportService( MissionParticipantsRepository missionParticipantsRepository, MissionRepository missionRepository, MissionReportRepository missionReportRepository) {
        this.missionReportRepository = missionReportRepository;
        this.missionRepository = missionRepository;
        this.missionParticipantsRepository = missionParticipantsRepository;
        this.modelMapper = new ModelMapper();
    }

    public MissionReportDTO getMissionReportById(int id) {
        return missionReportRepository.findById(id)
                .map(missionReport -> modelMapper.map(missionReport, MissionReportDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Mission report not found with id: " + id));
    }

    public List<MissionReportDTO> getAllMissionReports() {
        List<MissionReport> missionReports = missionReportRepository.findAll();
        return missionReports.stream()
                .map(missionReport -> modelMapper.map(missionReport, MissionReportDTO.class))
                .toList();
    }


    public MissionReportDTO getMissionReportByMissionIdWithDetails(int missionId) {
        MissionReport missionReport = missionReportRepository.getMissionReportByMission_Id(missionId)
                .orElseThrow(() -> new RuntimeException("Mission report not found with mission id"+missionId));
        return  MissionReportDTO.withDetails(missionReport);    }

}
