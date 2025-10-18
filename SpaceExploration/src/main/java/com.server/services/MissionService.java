package com.server.services;


import com.server.dto.*;
import com.server.models.Destination;
import com.server.models.Mission;
import com.server.models.MissionParticipants;
import com.server.models.MissionSpecialization;
import com.server.repositories.DestinationRepository;
import com.server.repositories.MissionParticipantsRepository;
import com.server.repositories.MissionReportRepository;
import com.server.repositories.MissionRepository;
import com.server.util.MissionResult;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MissionService {
    private final MissionRepository missionRepository;
    private final DestinationRepository destinationRepository;
    private final ModelMapper modelMapper;


    public MissionService(ModelMapper modelMapper,
                          MissionRepository missionRepository,
                          DestinationRepository destinationRepository) {
        this.missionRepository = missionRepository;
        this.destinationRepository = destinationRepository;
        this.modelMapper = modelMapper;

    }

    @Transactional(readOnly = true)
    public MissionDTO getMissionById(int id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + id));
        return MissionDTO.missionWithDetails(mission);
    }

    @Transactional(readOnly = true)
    public List<MissionDTO> getAllMissions() {
        List<Mission> missions = missionRepository.findAll();
        return missions.stream().map(MissionDTO::missionWithDetails).toList();
    }

    @Transactional(readOnly = true)
    public List<MissionDTO> getMissionsByFilters(
            Mission.DifficultyLevel difficultyLevel,
            Integer destinationId) {

        List<Mission> missions;

        if (difficultyLevel != null && destinationId != null) {
            missions = missionRepository.findByDifficultyLevelAndDestinationId(difficultyLevel, destinationId);
        } else if (difficultyLevel != null) {
            missions = missionRepository.findByDifficultyLevel(difficultyLevel);
        } else if (destinationId != null) {
            missions = missionRepository.findByDestinationId(destinationId);
        } else {
            missions = missionRepository.findAll();
        }
        return missions.stream()
                .map(MissionDTO::missionWithDetails)
                .toList();
    }


    @Transactional
    public MissionDTO addMission(MissionDTO missionDTO) {
        if (missionRepository.existsByCode(missionDTO.getCode())) {
            throw new IllegalArgumentException("Mission with code '" + missionDTO.getCode() + "' already exists");
        }
        Destination destination = destinationRepository.findById(missionDTO.getDestinationId())
                .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + missionDTO.getDestinationId()));

        Mission mission = modelMapper.map(missionDTO, Mission.class);
        mission.setDestination(destination);
        missionRepository.save(mission);
        return modelMapper.map(mission, MissionDTO.class);
    }

    @Transactional
    public MissionDTO removeSpecialization(Integer missionId,
                                           MissionSpecialization.Specialization specialization) {
        Mission mission = findMissionById(missionId);
        mission.removeRequiredSpecialization(specialization);
        Mission savedMission = missionRepository.save(mission);
        return modelMapper.map(savedMission, MissionDTO.class);
    }
    @Transactional
    public MissionDTO addOrUpdateSpecializations(Integer missionId,
                                                 List<MissionSpecializationDTO.AddSpecializationRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Specializations list cannot be empty");
        }
        Mission mission = findMissionById(missionId);
        requests.forEach(request -> {
            try {
                MissionSpecialization.Specialization specialization =
                        MissionSpecialization.Specialization.fromString(request.getSpecialization());
                mission.updateSpecializationQuantity(specialization, request.getQuantity());

            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid specialization: " + request.getSpecialization() +
                        ". " + e.getMessage());
            }
        });
        validateMissionConstraints(mission);

        Mission savedMission = missionRepository.save(mission);
        return modelMapper.map(savedMission, MissionDTO.class);
    }


    private void validateMissionConstraints(Mission mission) {
        if (mission.getTotalRequiredCrew() > mission.getCrewSize()) {
            throw new IllegalArgumentException(
                    String.format("Required specialists (%d) exceed maximum crew size (%d)",
                            mission.getTotalRequiredCrew(), mission.getCrewSize())
            );
        }
    }

    @Transactional
    public MissionDTO updateMission(MissionDTO.@Valid MissionUpdateDTO missionDTO, int id) {
        Mission existingMission = missionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + id));
        updateEntityFromDTO(existingMission, missionDTO);
        Mission updatedMission = missionRepository.save(existingMission);
        return modelMapper.map(updatedMission, MissionDTO.class);
    }

    private void updateEntityFromDTO(Mission entity, MissionDTO.@Valid MissionUpdateDTO dto) {
        Optional.ofNullable(dto.getName())
                .filter(name -> !name.trim().isEmpty())
                .ifPresent(entity::setName);
        Optional.ofNullable(dto.getCode())
                .filter(code -> !code.trim().isEmpty())
                .ifPresent(code -> {
                    if (!entity.getCode().equals(code)) {
                        if (missionRepository.existsByCode(code)) {
                            throw new IllegalArgumentException("Mission with code '" + code + "' already exists");
                        }
                        entity.setCode(code);
                    }
                });
        Optional.ofNullable(dto.getDescription()).ifPresent(entity::setDescription);
        Optional.ofNullable(dto.getDurationDays()).ifPresent(entity::setDurationDays);
        Optional.ofNullable(dto.getCrewSize()).ifPresent(entity::setCrewSize);
        Optional.ofNullable(dto.getScoreValue()).ifPresent(entity::setScoreValue);
        Optional.ofNullable(dto.getImageUrl()).ifPresent(entity::setImgUrl);
        Optional.ofNullable(dto.getPotentialIssues()).ifPresent(entity::setPotentialIssues);
        Optional.ofNullable(dto.getDifficultyLevel()).ifPresent(entity::setDifficultyLevel);
        Optional.ofNullable(dto.getPaymentAmount()).ifPresent(entity::setPaymentAmount);
        Optional.ofNullable(dto.getDestinationId())
                .filter(destId -> destId > 0)
                .filter(destId -> entity.getDestination() == null || !destId.equals(entity.getDestination().getId()))
                .ifPresent(destId -> {
                    Destination destination = destinationRepository.findById(destId)
                            .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + destId));
                    entity.setDestination(destination);
                });
    }

    @Transactional
    public void deleteMission(int id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + id));
        missionRepository.delete(mission);
    }
    @Transactional(readOnly = true)
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

    private Mission findMissionById(Integer missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission with id: " + missionId + " not found"));
    }


    public MissionResult startMission(Integer missionId, List<MissionParticipantsDTO> participants) {
        int successChance = 100;
        List<String> issues = new ArrayList<>();
        int crewSizeDeficit = 0;
        List<String> missingSpecs = new ArrayList<>();
        List<String> notReadyAstronauts = new ArrayList<>();


        Mission mission = findMissionById(missionId);

        int missing = Math.max(0, mission.getCrewSize() - participants.size());
        if (missing > 0) {
            int penalty = missing * 10;
            successChance -= penalty;
            crewSizeDeficit = missing;
            issues.add("Crew size deficit: missing " + missing + " member(s) (-" + penalty + "% from success)");
            System.out.println("Missing "+ missing + " persons, -" + penalty + "% to success chance");
        } else {
            issues.add("Mission crew size is accepted!");
            System.out.println("Mission crew size is accepted!");
        }

        Set<MissionSpecialization> requiredSpecs = mission.getMissionSpecializations();
        if (requiredSpecs != null && !requiredSpecs.isEmpty()) {
            List<String> crewSpecs = participants.stream()
                    .map(p -> p.getSpecialization().name())
                    .distinct()
                    .toList();

            List<String> requiredSpecNames = requiredSpecs.stream()
                    .map(req -> req.getSpecialization().name())
                    .toList();

             missingSpecs = requiredSpecNames.stream()
                    .filter(req -> !crewSpecs.contains(req))
                    .toList();

            if (!missingSpecs.isEmpty()) {
                int specPenalty = missingSpecs.size() * 10;
                successChance -= specPenalty;
                System.out.println("Missing specializations: " + missingSpecs + ", -" + specPenalty + "% to success chance");
                issues.add("Missing specializations: " + missingSpecs.toString().toLowerCase() + ", -" + specPenalty + "% to success chance");

            } else {
                issues.add("All required specializations are present in the crew.");
                System.out.println("All required specializations are present in the crew.");
            }
        } else {
            System.out.println("No required specializations defined for this mission.");
        }

        for(MissionParticipantsDTO p : participants ){
            if(p.getHealthStatus().toString().equalsIgnoreCase("RETIRED")||p.getHealthStatus().toString().equalsIgnoreCase("MEDICAL_REVIEW")){
                successChance -= 10;
                System.out.println("Astronaut "+p.getAstronautName()+ " is not ready to flight!");
                String astronautName = p.getAstronautName();
                issues.add("Astronaut " + astronautName + " is not flight ready (Status: " + p.getHealthStatus() + ") (-10%)");
                notReadyAstronauts.add(astronautName + " (" + p.getHealthStatus() + ")");
            }
        }
        if (successChance < 0) successChance = 0;
        if (successChance > 100) successChance = 100;
        System.out.println("The final success chance is " + successChance);

        int alienChance = ThreadLocalRandom.current().nextInt(0, 100);
        boolean alienAttack = alienChance < 20;

        if (alienAttack) {
            issues.add("CRITICAL: Alien attack encountered during mission!");
            return MissionResult.builder()
                    .success(false)
                    .successChance(successChance)
                    .issues(issues)
                    .alienAttack(true)
                    .crewSizeDeficit(crewSizeDeficit)
                    .missingSpecializations(missingSpecs)
                    .notReadyAstronauts(notReadyAstronauts)
                    .build();
        }

        int roll = ThreadLocalRandom.current().nextInt(0, 100);

        boolean success = roll < successChance;


        return MissionResult.builder()
                .success(success)
                .successChance(successChance)
                .issues(issues)
                .alienAttack(false)
                .crewSizeDeficit(crewSizeDeficit)
                .missingSpecializations(missingSpecs)
                .notReadyAstronauts(notReadyAstronauts)
                .build();
    }
}









