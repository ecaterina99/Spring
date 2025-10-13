package com.client.controllers;

import com.client.DTO.AstronautDTO;
import com.client.DTO.MissionDTO;
import com.client.DTO.MissionParticipantsDTO;
import com.client.exceptions.ApiProxyException;
import com.client.service.AstronautService;
import com.client.service.MissionParticipantsService;
import com.client.service.MissionService;
import com.client.service.TokenStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

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
            Model model) {

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

/*
    @PostMapping ("/add/{missionId}/{astronautId}")
    public String addAstronautToMission(
            @PathVariable Integer missionId,
            @PathVariable Integer astronautId,
     RedirectAttributes redirectAttributes) {

        try {
            missionParticipantsService.addParticipants(missionId, astronautId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Astronaut added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to add astronaut. Please try again.");
        }
        return "redirect:/mission-participants?missionId=" + missionId;
    }

 */

}
