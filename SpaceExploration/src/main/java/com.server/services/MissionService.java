package com.server.services;

import com.server.dto.MissionDTO;
import com.server.models.Destination;
import com.server.models.Mission;
import com.server.repositories.DestinationRepository;
import com.server.repositories.MissionParticipantsRepository;
import com.server.repositories.MissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class MissionService {
    private final MissionRepository missionRepository;
    private final DestinationRepository destinationRepository;
    private final ModelMapper modelMapper;
    private final MissionParticipantsRepository missionParticipantsRepository;

    public MissionService(ModelMapper modelMapper,
                          MissionRepository missionRepository,
                          DestinationRepository destinationRepository, MissionParticipantsRepository missionParticipantsRepository) {
        this.missionRepository = missionRepository;
        this.destinationRepository = destinationRepository;
        this.modelMapper = modelMapper;
        this.missionParticipantsRepository = missionParticipantsRepository;
    }

    public MissionDTO getMissionById(int id) {
        return missionRepository.findById(id)
                .map(mission -> modelMapper.map(mission, MissionDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + id));
    }

    public List<MissionDTO> getAllMissions() {

        List<Mission> missions = missionRepository.findAll();
        return missions.stream()
                .map(mission -> modelMapper.map(mission, MissionDTO.class))
                .toList();
    }

    public MissionDTO addMission(MissionDTO missionDTO) {
        // Check if mission code already exists
        if (missionRepository.existsByCode(missionDTO.getCode())) {
            throw new IllegalArgumentException("Mission with code '" + missionDTO.getCode() + "' already exists");
        }
        // Validate and fetch destination
        Destination destination = destinationRepository.findById(missionDTO.getDestinationId())
                .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + missionDTO.getDestinationId()));
        Mission mission = modelMapper.map(missionDTO, Mission.class);
        mission.setDestination(destination);
        Mission savedMission = missionRepository.save(mission);
        return modelMapper.map(savedMission, MissionDTO.class);
    }

    public MissionDTO updateMission(MissionDTO missionDTO, int id) {
        Mission existingMission = missionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + id));
        if (missionDTO.getMissionName() != null && !missionDTO.getMissionName().trim().isEmpty()) {
            existingMission.setMissionName(missionDTO.getMissionName());
        }
        if (missionDTO.getCode() != null && !missionDTO.getCode().trim().isEmpty()) {
            if (!existingMission.getCode().equals(missionDTO.getCode())) {
                if (missionRepository.existsByCode(missionDTO.getCode())) {
                    throw new IllegalArgumentException("Mission with code '" + missionDTO.getCode() + "' already exists");
                }
            }
            existingMission.setCode(missionDTO.getCode());
        }
        if (missionDTO.getDescription() != null && !missionDTO.getDescription().trim().isEmpty()) {
            existingMission.setDescription(missionDTO.getDescription());
        }
        if (missionDTO.getDurationDays() > 0) {
            existingMission.setDurationDays(missionDTO.getDurationDays());
        }
        if (missionDTO.getCrewSizeRequired() > 0) {
            existingMission.setCrewSizeRequired(missionDTO.getCrewSizeRequired());
        }
        if (missionDTO.getRequiredSpecializations() != null && !missionDTO.getRequiredSpecializations().trim().isEmpty()) {
            existingMission.setRequiredSpecializations(missionDTO.getRequiredSpecializations());
        }
        if (missionDTO.getScoreValue() >= 0) {
            existingMission.setScoreValue(missionDTO.getScoreValue());
        }
        if (missionDTO.getPotentialIssues() != null && !missionDTO.getPotentialIssues().trim().isEmpty()) {
            existingMission.setPotentialIssues(missionDTO.getPotentialIssues());
        }
        if (missionDTO.getImage() != null) {
            existingMission.setImage(missionDTO.getImage());
        }
        if (missionDTO.getDifficultyLevel() != null) {
            existingMission.setDifficultyLevel(missionDTO.getDifficultyLevel());
        }
        if (missionDTO.getDestinationId() > 0 && existingMission.getDestination().getId() != missionDTO.getDestinationId()) {
            Destination destination = destinationRepository.findById(missionDTO.getDestinationId())
                    .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + missionDTO.getDestinationId()));
            existingMission.setDestination(destination);
        }
        Mission savedMission = missionRepository.save(existingMission);
        return modelMapper.map(savedMission, MissionDTO.class);
    }

    public void deleteMission(int id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + id));
        missionRepository.delete(mission);
    }

    public List<MissionDTO> getMissionsByDestinationId(int destinationId) {
        if (!destinationRepository.existsById(destinationId)) {
            throw new EntityNotFoundException("Destination not found with id: " + destinationId);
        }
        List<Mission> missions = missionRepository.findByDestinationId(destinationId);

        return missions.stream().map(mission ->
        {
            MissionDTO dto = modelMapper.map(mission, MissionDTO.class);
            dto.setDestinationName(mission.getDestination().getDestinationName());
            return dto;
        }).toList();
    }
}




