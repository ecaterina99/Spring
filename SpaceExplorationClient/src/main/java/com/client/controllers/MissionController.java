package com.client.controllers;
import com.client.DTO.AstronautDTO;
import com.client.DTO.FilterCriteriaDTO;
import com.client.DTO.MissionDTO;
import com.client.service.MissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/missions")
@Slf4j
public class MissionController {
    private final MissionService missionService;
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping
    public String getAllMissions(
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(required = false) Integer destinationId,
            Model model) {

        List<MissionDTO> missions = missionService.getMissionsByFilters(difficultyLevel, destinationId);

        Map<Integer, String> uniqueDestinations = missions.stream()
                .collect(Collectors.toMap(
                        MissionDTO::getDestinationId,
                        MissionDTO::getDestinationName,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        model.addAttribute("missions", missions);
        model.addAttribute("destinations", uniqueDestinations);
        model.addAttribute("filterCriteria", new FilterCriteriaDTO(difficultyLevel, destinationId));
        model.addAttribute("pageTitle", "All missions");
        model.addAttribute("totalCount", missions.size());

        return "missions";
    }

    @GetMapping("/filter-fragment")
    public String filterMissionsFragment(
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(required = false) Integer destinationId, Model model) {

        log.info("Filter fragment requested: difficulty={}, destination={}",
                difficultyLevel, destinationId);

        List<MissionDTO> missions = missionService.getMissionsByFilters(difficultyLevel, destinationId);
        model.addAttribute("missions", missions);
        return "mission-cards :: missionCards";
    }

    @GetMapping("/{id}")
    public String getAstronaut(@PathVariable int id, Model model) {
        log.debug("Handling request to display mission details for ID: {}", id);

        MissionDTO mission = missionService.getMissionById(id);

        model.addAttribute("mission", mission);
        model.addAttribute("pageTitle", "Mission Details - " + mission.getName());

        log.debug("Rendering mission details view for: {}",
                mission.getName());
        return "mission-details";
    }

}
