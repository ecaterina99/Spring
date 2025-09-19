package com.server.services;

import com.server.dto.MissionParticipantsDTO;
import com.server.models.Mission;
import com.server.models.MissionParticipants;
import com.server.repositories.AstronautRepository;
import com.server.repositories.MissionParticipantsRepository;
import com.server.repositories.MissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MissionParticipantsService {

private final MissionParticipantsRepository missionParticipantsRepository;
private final ModelMapper modelMapper;
private final MissionRepository missionRepository;
private final AstronautRepository astronautRepository;

public MissionParticipantsService(MissionParticipantsRepository missionParticipantsRepository,
                                  ModelMapper modelMapper, MissionRepository missionRepository,
                                  AstronautRepository astronautRepository) {
    this.missionParticipantsRepository = missionParticipantsRepository;
    this.modelMapper = modelMapper;
    this.missionRepository = missionRepository;
    this.astronautRepository = astronautRepository;
}

    public List<MissionParticipantsDTO> getAllParticipantsByMissionId(int missionId) {
        if (!missionRepository.existsById(missionId)) {
            throw new EntityNotFoundException("Mission not found with id: " + missionId);
        }

        List<MissionParticipants> participants = missionParticipantsRepository.findByMissionId(missionId);
        return participants.stream()
                .map(MissionParticipantsDTO::missionParticipantsDetails)
                .collect(Collectors.toList());
    }
}


