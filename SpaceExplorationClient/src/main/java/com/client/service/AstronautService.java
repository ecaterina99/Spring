package com.client.service;

import com.client.DTO.AstronautDTO;
import com.client.exceptions.ClientServiceException;
import com.client.exceptions.ResourceNotFoundException;
import com.client.exceptions.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class AstronautService {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public AstronautService(RestTemplate restTemplate, @Value("${api.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl + "/astronauts";
    }

    public List<AstronautDTO> getAllAstronauts() {
        log.info("Fetching all astronauts from API endpoint: {}", apiUrl);
        long startTime = System.currentTimeMillis();

        try {
            ResponseEntity<AstronautDTO[]> response =
                    restTemplate.getForEntity(apiUrl, AstronautDTO[].class);

            if (response.getBody() == null) {
                log.warn("Received null response body when fetching all astronauts");
                return new ArrayList<>();
            }

            List<AstronautDTO> astronauts = Arrays.asList(response.getBody());
            long duration = System.currentTimeMillis() - startTime;

            log.info("Successfully fetched {} astronauts in {}ms", astronauts.size(), duration);
            return astronauts;

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Astronauts endpoint not found: {}", e.getMessage());
            throw new ResourceNotFoundException("No astronauts available at this time");

        } catch (HttpClientErrorException e) {
            log.error("Client error while fetching astronauts. Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new ClientServiceException("Failed to retrieve astronauts due to client error");

        } catch (HttpServerErrorException e) {
            log.error("Server error while fetching astronauts. Status: {}, Message: {}",
                    e.getStatusCode(), e.getMessage());
            throw new ServiceUnavailableException("Server is experiencing issues. Please try again later.");

        } catch (ResourceAccessException e) {
            log.error("Network connectivity error while fetching astronauts: {}", e.getMessage());
            throw new ServiceUnavailableException("Unable to connect to the service. Please check your connection and try again.");

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Unexpected error occurred while fetching astronauts after {}ms: {}",
                    duration, e.getMessage(), e);
            throw new ClientServiceException("An unexpected error occurred while loading astronauts", e);
        }
    }

    public AstronautDTO getAstronautById(int id) {
        if (id <= 0) {
            log.warn("Invalid astronaut ID provided: {}", id);
            throw new IllegalArgumentException("Astronaut ID must be a positive number");
        }

        String url = apiUrl + "/" + id;
        log.info("Fetching astronaut with ID {} from: {}", id, url);
        long startTime = System.currentTimeMillis();

        try {
            AstronautDTO astronaut = restTemplate.getForObject(url, AstronautDTO.class);

            if (astronaut == null) {
                log.warn("No astronaut data received for ID: {}", id);
                throw new ResourceNotFoundException("Astronaut", id);
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("Successfully fetched astronaut '{}' (ID: {}) in {}ms",
                    astronaut.getFirstName() + " " + astronaut.getLastName(), id, duration);

            return astronaut;

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Astronaut with ID {} not found. Response: {}", id, e.getResponseBodyAsString());
            throw new ResourceNotFoundException("Astronaut", id);

        } catch (HttpClientErrorException e) {
            log.error("Client error while fetching astronaut {}. Status: {}, Body: {}",
                    id, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ClientServiceException("Failed to retrieve astronaut due to client error");

        } catch (HttpServerErrorException e) {
            log.error("Server error while fetching astronaut {}. Status: {}, Message: {}",
                    id, e.getStatusCode(), e.getMessage());
            throw new ServiceUnavailableException("Server is experiencing issues. Please try again later.");

        } catch (ResourceAccessException e) {
            log.error("Network connectivity error while fetching astronaut {}: {}", id, e.getMessage());
            throw new ServiceUnavailableException("Unable to connect to the service. Please check your connection and try again.");

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Unexpected error occurred while fetching astronaut {} after {}ms: {}",
                    id, duration, e.getMessage(), e);
            throw new ClientServiceException("An unexpected error occurred while loading astronaut details", e);
        }
    }

    public boolean isServiceHealthy() {
        try {
            log.debug("Performing health check on astronauts service");
            restTemplate.getForEntity(apiUrl, AstronautDTO[].class);
            return true;
        } catch (Exception e) {
            log.warn("Service health check failed: {}", e.getMessage());
            return false;
        }
    }
}