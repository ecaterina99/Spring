package com.server.services;

import com.server.dto.DestinationDTO;
import com.server.models.Destination;
import com.server.repositories.DestinationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final ModelMapper modelMapper;

    public DestinationService(DestinationRepository destinationRepository, ModelMapper modelMapper) {
        this.destinationRepository = destinationRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public DestinationDTO getDestinationById(int id) {
        return destinationRepository.findById(id)
                .map(destination -> modelMapper.map(destination, DestinationDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public DestinationDTO.DestinationWithMissionsDTO getDestinationByIdWithMission(int id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Destination with id: " + id + " not found"));
        return DestinationDTO.DestinationWithMissionsDTO.DestinationWithMissions(destination);
    }

    @Transactional(readOnly = true)
    public List<DestinationDTO> getAllDestinations() {
        List<Destination> destinations = destinationRepository.findAll();
        return destinations.stream()
                .map(destination -> modelMapper.map(destination, DestinationDTO.class))
                .toList();
    }
}
