package com.server.services;

import com.server.dto.MissionParticipantsDTO;
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
    @Transactional(readOnly = true)
    public List<MissionParticipantsDTO> showMissionCrew(int missionId) {
        if (!missionRepository.existsById(missionId)) {
            throw new EntityNotFoundException("Mission not found with id: " + missionId);
        }
        List<MissionParticipants> participants = missionParticipantsRepository.findByMissionId(missionId);
        return participants.stream()
                .map(MissionParticipantsDTO::missionParticipantsDetails)
                .collect(Collectors.toList());
    }
    @Transactional
    public MissionParticipantsDTO addParticipantsToMission(int missionId, int astronautId) {
        if (!missionRepository.existsById(missionId)) {
            throw new EntityNotFoundException("Mission not found with id: " + missionId);
        }
        if (!astronautRepository.existsById(astronautId)) {
            throw new EntityNotFoundException("Astronaut not found");
        }
        if (missionParticipantsRepository.existsByMissionIdAndAstronautId(missionId, astronautId)) {
            throw new IllegalStateException("Astronaut is already assigned to this mission");
        }
        MissionParticipants missionParticipants = new MissionParticipants();
        missionParticipants.setAstronaut(astronautRepository.findById(astronautId).get());
        missionParticipants.setMission(missionRepository.findById(missionId).get());
        missionParticipantsRepository.save(missionParticipants);
        return modelMapper.map(missionParticipants, MissionParticipantsDTO.class);
    }
}


