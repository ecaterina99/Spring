package com.client.service;

import com.client.DTO.DestinationDTO;
import com.client.helpers.RestClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service class responsible for retrieving destination data from the external API.
 * Uses RestClientUtil to perform REST requests and fetch a list of all destinations.
 * The API endpoint base URL is injected from application properties.
 */
@Service
@Slf4j
public class DestinationService {
    private final String apiUrl;
    private final RestClientUtil restUtil;

    public DestinationService(@Value("${api.url}") String apiUrl, RestClientUtil restUtil) {
        this.apiUrl = apiUrl + "/destinations";
        this.restUtil = restUtil;
    }

    public List<DestinationDTO> getAllDestinations() {
        return restUtil.getList(apiUrl, DestinationDTO[].class);
    }

    public DestinationDTO getDestinationByIdWithMission(int id) {

        validateId(id);
        return restUtil.getObject(apiUrl + "/missions/" + id, DestinationDTO.class);
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Destination" + " ID must be positive");
        }
    }
}
