package com.client.controllers;
import com.client.DTO.MissionDTO;
import com.client.service.MissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/missions")
@Slf4j
public class MissionController {
    private final MissionService missionService;
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }
    @GetMapping
    public String getAllMissions(Model model) {
        log.debug("Handling request to display all missions");

        List<MissionDTO> missions = missionService.getAllMissions();
        model.addAttribute("missions", missions);
        model.addAttribute("pageTitle", "All missions");
        model.addAttribute("totalCount", missions.size());
        log.debug("Rendering missions-list.html list view with {} missions", missions.size());
        return "missions-list";
    }

}
