package com.client.controllers;

import com.client.DTO.AstronautDTO;
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
public class AstronautController {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public AstronautController(RestTemplate restTemplate, @Value("${api.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl + "/astronauts";
    }

    @GetMapping
    public String getAllAstronauts(Model model) {
        ResponseEntity<AstronautDTO[]> response =
                restTemplate.getForEntity(apiUrl, AstronautDTO[].class);

        List<AstronautDTO> astronauts = Arrays.asList(response.getBody());
        model.addAttribute("astronauts", astronauts);
        return "astronauts-list";
    }

    @GetMapping("/{id}")
    public String getAstronaut(@PathVariable int id, Model model) {
        AstronautDTO astronaut =
                restTemplate.getForObject(apiUrl + "/" + id, AstronautDTO.class);

        model.addAttribute("astronaut", astronaut);
        return "astronaut-details";
    }
}