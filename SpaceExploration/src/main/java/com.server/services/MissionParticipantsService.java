package com.server.services;

import com.server.dto.MissionParticipantsDTO;
import com.server.dto.MissionSpecializationDTO;
import com.server.exception.CrewOverflowException;
import com.server.models.Astronaut;
import com.server.models.Mission;
import com.server.models.MissionParticipants;
import com.server.models.MissionSpecialization;
import com.server.repositories.AstronautRepository;
import com.server.repositories.MissionParticipantsRepository;
import com.server.repositories.MissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing mission participants.
 * It provides functionality to:
 * Retrieve all astronauts assigned to a mission.
 * Add new astronauts to a mission while validating crew limits.
 * Remove all participants from a mission.
 */

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
            throw new EntityNotFoundException("Mission with id: " + missionId + " is not found");
        }
        List<MissionParticipants> participants = missionParticipantsRepository.findByMissionId(missionId);
        return participants.stream()
                .map(MissionParticipantsDTO::missionParticipantsDetails)
                .collect(Collectors.toList());
    }


    public void clearMissionCrew(int missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + missionId));

        List<MissionParticipants> existingParticipants = missionParticipantsRepository.findByMissionId(missionId);

        if (!existingParticipants.isEmpty()) {
            missionParticipantsRepository.deleteAll(existingParticipants);
        }
    }

    @Transactional
    public MissionParticipantsDTO addParticipantsToMission(int missionId, int astronautId) {

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission with id: " + missionId + " is not found"));

        Astronaut astronaut = astronautRepository.findById(astronautId)
                .orElseThrow(() -> new EntityNotFoundException("Astronaut with id: " + astronautId + " is not found"));

        if (missionParticipantsRepository.existsByMissionIdAndAstronautId(missionId, astronautId)) {
            throw new IllegalStateException(astronaut.getFirstName() + " " + astronaut.getLastName() + " is already assigned to this mission!");
        }

        int currentCrewSize = missionParticipantsRepository.findByMissionId(missionId).size();
        int maxCrewSize = mission.getCrewSize();

        if (currentCrewSize >= maxCrewSize) {
            throw new CrewOverflowException(currentCrewSize, maxCrewSize);
        }

        MissionParticipants missionParticipants = new MissionParticipants();
        missionParticipants.setMission(mission);
        missionParticipants.setAstronaut(astronaut);
        missionParticipantsRepository.save(missionParticipants);

        return modelMapper.map(missionParticipants, MissionParticipantsDTO.class);
    }
}
