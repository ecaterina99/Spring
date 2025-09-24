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
public class MissionReportService {
    private final MissionReportRepository missionReportRepository;
    private final ModelMapper modelMapper;

    public MissionReportService(MissionReportRepository missionReportRepository) {
        this.missionReportRepository = missionReportRepository;
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
}



 /*   public MissionReportDTO getMissionReportByMissionIdWithDetails(int missionId) {
        MissionReport missionReport = missionReportRepository.getMissionReportByMission_Id(missionId)
                .orElseThrow(() -> new RuntimeException("Mission report not found with mission id"+missionId));
        return  MissionReportDTO.(missionReport);    }

  */