package com.client.controllers;

import com.client.DTO.AstronautDTO;
import com.client.DTO.MissionDTO;
import com.client.DTO.MissionParticipantsDTO;
import com.client.service.AstronautService;
import com.client.service.MissionParticipantsService;
import com.client.service.MissionService;
import com.client.service.TokenStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mission-participants")
@Slf4j
public class MissionParticipantsController {

    private final MissionParticipantsService missionParticipantsService;
    private final MissionService missionService;
    private final AstronautService astronautService;

    public MissionParticipantsController(MissionParticipantsService missionParticipantsService, MissionService missionService, AstronautService astronautService, TokenStorage tokenStorage) {
        this.missionParticipantsService = missionParticipantsService;
        this.missionService = missionService;
        this.astronautService = astronautService;
    }

    @GetMapping
    public String getAllParticipants(
            @RequestParam(required = false) Integer missionId,
            Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if (missionId == null) {
            return "redirect:/missions";
        }

        MissionDTO mission = missionService.getMissionById(missionId);

        List<MissionParticipantsDTO> participants = missionParticipantsService.getAllParticipants(missionId);
        List<AstronautDTO> astronauts = astronautService.getAllAstronauts();

        model.addAttribute("astronauts", astronauts);
        model.addAttribute("mission", mission);
        model.addAttribute("participants", participants);
        model.addAttribute("pageTitle", "Mission Preparation");

        return "mission-preparation";
    }
}
