package com.server.services;

import com.server.dto.MissionParticipantsDTO;
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
import java.util.Optional;
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

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + missionId));

        Astronaut astronaut = astronautRepository.findById(astronautId)
                .orElseThrow(() -> new EntityNotFoundException("Astronaut not found with id: " + astronautId));

        if (missionParticipantsRepository.existsByMissionIdAndAstronautId(missionId, astronautId)) {
            throw new IllegalStateException("Astronaut is already assigned to this mission");
        }

        int currentCrewSize = missionParticipantsRepository.findByMissionId(missionId).size();
        int maxCrewSize = mission.getCrewSize();

        if (currentCrewSize >= maxCrewSize) {
            throw new IllegalStateException("The crew for this mission is already full (" + maxCrewSize + " members).");
        }
        List<MissionSpecialization> requiredSpecs = (List<MissionSpecialization>) mission.getMissionSpecializations();
        Map<Astronaut.Specialization, Long> currentSpecCount = missionParticipantsRepository.findByMissionId(missionId)
                .stream()
                .collect(Collectors.groupingBy(
                        mp -> mp.getAstronaut().getSpecialization(),
                        Collectors.counting()
                ));
        Astronaut.Specialization astronautSpec = astronaut.getSpecialization();
        Optional<MissionSpecialization> required = requiredSpecs.stream()
                .filter(r -> r.getSpecialization().name().equals(astronautSpec.name()))
                .findFirst();

        if (required.isEmpty()) {
            throw new IllegalStateException("This mission does not require specialization: " + astronautSpec);
        }

        long alreadyHave = currentSpecCount.getOrDefault(astronautSpec, 0L);
        if (alreadyHave >= required.get().getQuantity()) {
            throw new IllegalStateException("Mission already has enough " + astronautSpec + " specialists (" + required.get().getQuantity() + ").");
        }

        MissionParticipants missionParticipants = new MissionParticipants();
        missionParticipants.setMission(mission);
        missionParticipants.setAstronaut(astronaut);
        missionParticipantsRepository.save(missionParticipants);

        boolean allFilled = true;

        for (MissionSpecialization req : requiredSpecs) {
            long currentCount = currentSpecCount.getOrDefault(req.getSpecialization(), 0L);
            long updatedCount = currentCount;
            if (req.getSpecialization().name().equals(astronautSpec.name())) {
                updatedCount++;
            }
            if (updatedCount < req.getQuantity()) {
                allFilled = false;
                break;
            }
        }

        if (!allFilled) {
            throw new IllegalStateException("Not all required specializations are yet filled for this mission.");
        }

        return modelMapper.map(missionParticipants, MissionParticipantsDTO.class);
    }
}


