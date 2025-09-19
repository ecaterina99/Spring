package com.server.services;

import com.server.dto.MissionDTO;
import com.server.dto.MissionRequiredSpecializationDTO;
import com.server.models.Destination;
import com.server.models.Mission;
import com.server.models.MissionRequiredSpecializations;
import com.server.repositories.DestinationRepository;
import com.server.repositories.MissionParticipantsRepository;
import com.server.repositories.MissionRepository;
import com.server.repositories.MissionRequiredSpecializationsRepository;
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
    private final MissionRequiredSpecializationsRepository specializationsRepository;


    public MissionService(ModelMapper modelMapper,
                          MissionRepository missionRepository,
                          DestinationRepository destinationRepository, MissionParticipantsRepository missionParticipantsRepository,
                          MissionRequiredSpecializationsRepository specializationsRepository) {
        this.missionRepository = missionRepository;
        this.destinationRepository = destinationRepository;
        this.modelMapper = modelMapper;
        this.missionParticipantsRepository = missionParticipantsRepository;
        this.specializationsRepository = specializationsRepository;
    }
    @Transactional(readOnly = true)
    public MissionDTO getMissionById(int id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Mission not found with id: "+ id));
        return MissionDTO.withDetails(mission);
    }
    @Transactional(readOnly = true)
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

        if (missionDTO.getRequiredSpecializations() != null && !missionDTO.getRequiredSpecializations().isEmpty()) {
            for (MissionRequiredSpecializationDTO specDTO : missionDTO.getRequiredSpecializations()) {
                MissionRequiredSpecializations spec = new MissionRequiredSpecializations();
                spec.setMission(savedMission);
                spec.setSpecialization(specDTO.getSpecialization());
                spec.setQuantityRequired(specDTO.getQuantityRequired());
                savedMission.getMissionRequiredSpecializations().add(spec);
            }
            savedMission = missionRepository.save(savedMission);
        }

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
        if (missionDTO.getRequiredSpecializations() != null) {
            updateMissionSpecializations(existingMission, missionDTO.getRequiredSpecializations());
        }
        Mission savedMission = missionRepository.save(existingMission);
        return modelMapper.map(savedMission, MissionDTO.class);
    }

    private void updateMissionSpecializations(Mission mission, List<MissionRequiredSpecializationDTO> newSpecs) {
        mission.getMissionRequiredSpecializations().clear();

        for (MissionRequiredSpecializationDTO specDTO : newSpecs) {
            MissionRequiredSpecializations spec = new MissionRequiredSpecializations();
            spec.setMission(mission);
            spec.setSpecialization(specDTO.getSpecialization());
            spec.setQuantityRequired(specDTO.getQuantityRequired());
            mission.getMissionRequiredSpecializations().add(spec);
        }
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



   /*

   public list<astronaut> createCrew(int astronautId, Mission mission){
   if (!astronautRepository.existsById(astronautId)) {
            throw new EntityNotFoundException("Astronaut not found with id: " + astronautId);

        } List<MissionParticipants> crew = new Array list();
        for(int i=0; i<crewRequaried; i++){
        crew.addAstronautToMission()
        }
        for(int i=0; i:crewRequaried; i++){
        if(astrasnaut.getSpecialization != specialization){
        "you have to select all requaried specializations!"
        } else if(crew.size< crewRequaried ||crew.size > crewRequaried ){
        please add exactly necessary number of astronauts
        }



   }


   public int getTheAstronautRating(int id, Mission mission) {
        int rating;
        rating = astronaut.getOverallScore + yearOfExpirience /2
    }

    public int getTheRiskOfFailure(){
        for(Astronaut a astronauts){
            ratingCrew = a.getRating ++;
        }

        boolean successful = false;

        ratingCrew= ratingCrew/missionParticipants.size();
        switch (successful){
            case mission.getDifficultyLevel()=='low'{
                if (ratingCrew <30){
                    successful = false;
                }else{
                    successful = true;
                }
            }break;
            case mission.getDifficultyLevel()=='medium'{
                if (ratingCrew <50){
                    successful = false;
                }else{
                    successful = true;
                }break;
                case mission.getDifficultyLevel()=='high'{
                    if (ratingCrew <70){
                        successful = false;
                    }else{
                        successful = true;
                    }break;
                    case mission.getDifficultyLevel()=='extreme'{
                        if (ratingCrew <90){
                            successful = false;
                        }else{
                            successful = true;
                        }break;
                        default:successful = true;
        }

    */





