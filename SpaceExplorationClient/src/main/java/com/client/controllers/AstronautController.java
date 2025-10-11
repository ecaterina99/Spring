package com.client.controllers;

import com.client.DTO.AstronautDTO;
import com.client.DTO.UserDTO;
import com.client.service.ApiClient;
import com.client.service.AstronautService;
import com.client.service.TokenStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/astronauts")
@Slf4j
public class AstronautController {

    private final AstronautService astronautService;


    public AstronautController(AstronautService astronautService, ApiClient apiClient, TokenStorage tokenStorage) {
        this.astronautService = astronautService;

    }

    @GetMapping
    public String getAllAstronauts(Model model) {
        log.debug("Handling request to display all astronauts");

        List<AstronautDTO> astronauts = astronautService.getAllAstronauts();
        model.addAttribute("astronauts", astronauts);
        model.addAttribute("pageTitle", "All Astronauts");
        model.addAttribute("totalCount", astronauts.size());

        log.debug("Rendering astronauts list view with {} astronauts", astronauts.size());
        return "astronauts-list";
    }
}
