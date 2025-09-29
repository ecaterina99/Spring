package com.client.controllers;

import com.client.DTO.DestinationDTO;
import com.client.service.DestinationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/destinations")
@Slf4j
public class DestinationController {
    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public String getAllDestinations(Model model) {
        log.debug("Handling request to display all destinations");

        List<DestinationDTO> destinations = destinationService.getAllDestinations();
        model.addAttribute("destinations", destinations);
        model.addAttribute("pageTitle", "All destinations");
        model.addAttribute("totalCount", destinations.size());
        log.debug("Rendering destination.html list view with {} destinations", destinations.size());
        return "destinations-list";
    }
    @GetMapping("/{id}")
    public String getDestination(@PathVariable int id, Model model) {
        log.debug("Handling request to display destination.html details for ID: {}", id);

        DestinationDTO destination = destinationService.getDestinationById(id);

        model.addAttribute("destination", destination);
        model.addAttribute("pageTitle", "Destination Details - " + destination.getDestinationName());

        log.debug("Rendering astronaut details view for:{}",
                destination.getDestinationName());
        return "destination";
    }
    @GetMapping("/interactive-map")
    public String getInteractiveMap(Model model) {
        log.debug("Handling request for interactive space map");

        List<DestinationDTO> destinations = destinationService.getAllDestinations();

        // DEBUG: Add this logging
        log.info("DEBUG: Retrieved {} destinations from database", destinations.size());
        destinations.forEach(dest ->
                log.info("DEBUG: Destination - ID: {}, Name: {}, Type: {}",
                        dest.getId(), dest.getDestinationName(), dest.getEntityType())
        );

        model.addAttribute("destinations", destinations);
        model.addAttribute("pageTitle", "Interactive Space Map");

        log.debug("Rendering interactive map with {} destinations", destinations.size());
        return "space";
    }
}
