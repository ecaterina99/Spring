package com.client.controllers;

import com.client.DTO.AstronautDTO;
import com.client.DTO.DestinationDTO;
import com.client.DTO.MissionDTO;
import com.client.service.AstronautService;
import com.client.service.DestinationService;
import com.client.service.MissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private final MissionService missionService;
    private final AstronautService astronautService;
    private final DestinationService destinationService;


    public HomeController(MissionService missionService, AstronautService astronautService, DestinationService destinationService) {
        this.missionService = missionService;
        this.astronautService = astronautService;
        this.destinationService = destinationService;
    }
    @GetMapping
    public String showHomePage (Model model,@RequestParam(required = false) String difficultyLevel,
                                @RequestParam(required = false) Integer destinationId){
        List<DestinationDTO> destinations = destinationService.getAllDestinations();
        List < MissionDTO> missions = missionService.getMissionsByFilters(difficultyLevel, destinationId);
        List <AstronautDTO> astronauts = astronautService.getAllAstronauts();

        model.addAttribute("destinations", destinations);
        model.addAttribute("missions", missions);
        model.addAttribute("astronauts", astronauts);
        return "home";
    }
}
