package com.server.services;

import com.server.dto.AstronautDTO;
import com.server.models.Astronaut;
import com.server.repositories.AstronautRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AstronautService {
    private final ModelMapper modelMapper;
    private final AstronautRepository astronautRepository;

    public AstronautService(ModelMapper modelMapper, AstronautRepository astronautRepository
    ) {
        this.modelMapper = modelMapper;
        this.astronautRepository = astronautRepository;
    }

    public AstronautDTO getAstronautById(int id) {
        Astronaut astronaut =  astronautRepository.findByIdWithMissions(id)
                .orElseThrow(() -> new EntityNotFoundException("Astronaut not found with id: " + id));
        return AstronautDTO.withMissionWithoutDetails(astronaut);
    }

    public AstronautDTO getAstronautWithMissions(int astronautId) {
        Astronaut astronaut = astronautRepository.findByIdWithMissions(astronautId)
                .orElseThrow(() -> new RuntimeException("Astronaut not found"));
        return  AstronautDTO.withMissions(astronaut);
    }

    public List<AstronautDTO> getAllAstronauts() {
        List<Astronaut> astronauts = astronautRepository.findAllWithMissions();
        return astronauts.stream()
                .map(AstronautDTO::withMissionWithoutDetails)
                .toList();
    }

    public AstronautDTO addAstronaut(AstronautDTO astronautDTO) {
        Astronaut astronaut = modelMapper.map(astronautDTO, Astronaut.class);
        Astronaut savedAstronaut = astronautRepository.save(astronaut);
        return modelMapper.map(savedAstronaut, AstronautDTO.class);
    }

    public AstronautDTO updateAstronaut(int id, AstronautDTO astronautDTO) {
        Astronaut existingAstronaut = astronautRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Astronaut not found with id: " + id));

        updateEntityFromDTO(existingAstronaut, astronautDTO);

        Astronaut updatedAstronaut = astronautRepository.save(existingAstronaut);
        return modelMapper.map(updatedAstronaut, AstronautDTO.class);
    }

    private void updateEntityFromDTO(Astronaut entity, AstronautDTO dto) {
        if (dto.getFullName() != null) {
            entity.setFullName(dto.getFullName());
        }
        if (dto.getYearsOfExperience() != 0) {
            entity.setYearsOfExperience(dto.getYearsOfExperience());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getDateOfBirth() != null) {
            entity.setDateOfBirth(dto.getDateOfBirth());
        }
        if (dto.getDailyRate() != null) {
            entity.setDailyRate(dto.getDailyRate());
        }
        if (dto.getFitnessScore() != null) {
            entity.setFitnessScore(dto.getFitnessScore());
        }
        if (dto.getEducationScore() != null) {
            entity.setEducationScore(dto.getEducationScore());
        }
        if (dto.getPsychologicalScore() != null) {
            entity.setPsychologicalScore(dto.getPsychologicalScore());
        }
        if (dto.getImage() != null) {
            entity.setImage(dto.getImage());
        }
        if (dto.getSpecialization() != null) {
            entity.setSpecialization(dto.getSpecialization());
        }
        if (dto.getHealthStatus() != null) {
            entity.setHealthStatus(dto.getHealthStatus());
        }
    }

    public void deleteAstronaut(int id) {
        Astronaut astronaut = astronautRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Astronaut not found with id: " + id));
        astronautRepository.delete(astronaut);
    }

}