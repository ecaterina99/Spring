package com.client.controllers;

import com.client.DTO.DestinationDTO;
import com.client.DTO.MissionDTO;
import com.client.service.DestinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
/**
 * Controller class responsible for handling web requests related to destinations.
 * Provides an endpoint to display an interactive mup of all destinations by retrieving data
 * from the DestinationService and rendering it in the corresponding view.
 */
@Controller
@RequestMapping("/destinations")
@Slf4j
public class DestinationController {
    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/interactive-map")
    public String getInteractiveMap(Model model) {
        log.debug("Handling request for interactive space map");

        List<DestinationDTO> destinations = destinationService.getAllDestinations();

        model.addAttribute("destinations", destinations);
        model.addAttribute("pageTitle", "Interactive Space Map");

        log.debug("Rendering interactive map with {} destinations", destinations.size());
        return "space";
    }

    @GetMapping("/missions/{id}")
    @ResponseBody
    public List<MissionDTO> getMissionsByDestination(@PathVariable int id) {
        log.debug("Fetching missions for destination ID: {}", id);
        DestinationDTO destination = destinationService.getDestinationByIdWithMission(id);

        List<MissionDTO> missions = destination.getMissions();

        if (!missions.isEmpty()) {
            MissionDTO first = missions.get(0);
            log.debug("BEFORE JSON: id={}, name={}, code='{}', description='{}'",
                    first.getId(), first.getName(), first.getCode(), first.getDescription());
        }
        return missions;
    }
}
