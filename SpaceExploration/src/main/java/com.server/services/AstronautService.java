package com.server.services;

import com.server.dto.AstronautDTO;
import com.server.models.Astronaut;
import com.server.repositories.AstronautRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



@Service
public class AstronautService {
    private final ModelMapper modelMapper;
    private final AstronautRepository astronautRepository;

    public AstronautService(ModelMapper modelMapper, AstronautRepository astronautRepository
    ) {
        this.modelMapper = modelMapper;
        this.astronautRepository = astronautRepository;
    }

    @Transactional(readOnly = true)
    public AstronautDTO getAstronautById(int id) {
        Astronaut astronaut = astronautRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Astronaut not found with id: " + id));
        return modelMapper.map(astronaut, AstronautDTO.class);
    }

    @Transactional(readOnly = true)
    public List<AstronautDTO> getAllAstronauts() {
        List<Astronaut> astronauts = astronautRepository.findAll();
        return astronauts.stream()
                .map(astronaut -> modelMapper.map(astronaut, AstronautDTO.class))
                .toList();
    }

    @Transactional
    public AstronautDTO addAstronaut(AstronautDTO astronautDTO) {
        Astronaut astronaut = modelMapper.map(astronautDTO, Astronaut.class);
        Astronaut savedAstronaut = astronautRepository.save(astronaut);
        return modelMapper.map(savedAstronaut, AstronautDTO.class);
    }

    @Transactional
    public AstronautDTO updateAstronaut(int id, AstronautDTO astronautDTO) {
        Astronaut existingAstronaut = astronautRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Astronaut not found with id: " + id));

        updateEntityFromDTO(existingAstronaut, astronautDTO);

        Astronaut updatedAstronaut = astronautRepository.save(existingAstronaut);
        return modelMapper.map(updatedAstronaut, AstronautDTO.class);
    }

    private void updateEntityFromDTO(Astronaut entity, AstronautDTO dto) {
        Optional.ofNullable(dto.getFirstName()).ifPresent(entity::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(entity::setLastName);
        Optional.ofNullable(dto.getYearsOfExperience()).ifPresent(entity::setYearsOfExperience);
        Optional.ofNullable(dto.getPhone()).ifPresent(entity::setPhone);
        Optional.ofNullable(dto.getDateOfBirth()).ifPresent(entity::setDateOfBirth);
        Optional.ofNullable(dto.getDailyRate()).ifPresent(entity::setDailyRate);
        Optional.ofNullable(dto.getFitnessScore()).ifPresent(entity::setFitnessScore);
        Optional.ofNullable(dto.getEducationScore()).ifPresent(entity::setEducationScore);
        Optional.ofNullable(dto.getPsychologicalScore()).ifPresent(entity::setPsychologicalScore);
        Optional.ofNullable(dto.getImageUrl()).ifPresent(entity::setImageUrl);
        Optional.ofNullable(dto.getSpecialization()).ifPresent(entity::setSpecialization);
        Optional.ofNullable(dto.getHealthStatus()).ifPresent(entity::setHealthStatus);
    }

    @Transactional
    public void deleteAstronaut(int id) {
        if (!astronautRepository.existsById(id)) {
            throw new EntityNotFoundException("Astronaut not found with id: " + id);
        }
        astronautRepository.deleteById(id);
    }
}