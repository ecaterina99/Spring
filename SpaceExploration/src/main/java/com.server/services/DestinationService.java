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
@Transactional
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final ModelMapper modelMapper;

    public DestinationService(DestinationRepository destinationRepository, ModelMapper modelMapper) {
        this.destinationRepository = destinationRepository;
        this.modelMapper = modelMapper;
    }

    public DestinationDTO getDestinationById(int id) {
        return destinationRepository.findById(id)
                .map(destination -> modelMapper.map(destination, DestinationDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + id));
    }

    public List<DestinationDTO> getAllDestinations() {

        List<Destination> destinations = destinationRepository.findAll();
        return destinations.stream()
                .map(destination -> modelMapper.map(destination, DestinationDTO.class))
                .toList();
    }
}
