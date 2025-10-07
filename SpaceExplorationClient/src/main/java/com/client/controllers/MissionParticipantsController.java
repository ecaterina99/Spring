package com.client.controllers;

import com.client.DTO.AstronautDTO;
import com.client.DTO.MissionDTO;
import com.client.DTO.MissionParticipantsDTO;
import com.client.service.AstronautService;
import com.client.service.MissionParticipantsService;
import com.client.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mission-participants")
@Slf4j
public class MissionParticipantsController {

    private final MissionParticipantsService missionParticipantsService;
    private final MissionService missionService;
    private final AstronautService astronautService;


    public MissionParticipantsController(MissionParticipantsService missionParticipantsService, MissionService missionService, AstronautService astronautService) {
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


    @PostMapping ("/add/{missionId}/{astronautId}")
    public String addAstronautToMission(
            @PathVariable Integer missionId,
            @PathVariable Integer astronautId) {

        missionParticipantsService.addParticipants(missionId, astronautId);

        return "redirect:/mission-participants?missionId=" + missionId;
    }

}
