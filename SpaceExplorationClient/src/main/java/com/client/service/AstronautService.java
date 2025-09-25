package com.client.service;

import com.client.DTO.AstronautDTO;
import com.client.exceptions.ClientServiceException;
import com.client.exceptions.ResourceNotFoundException;
import com.client.exceptions.ServiceUnavailableException;
import com.client.helpers.RestClientUtil;
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

    private final String apiUrl;
    private final RestClientUtil restUtil;


    public AstronautService( @Value("${api.url}") String apiUrl, RestClientUtil restUtil) {
        this.apiUrl = apiUrl + "/astronauts";
        this.restUtil = restUtil;
    }
    
    public List<AstronautDTO> getAllAstronauts() {
        return restUtil.getList(apiUrl, AstronautDTO[].class);
    }

    public AstronautDTO getAstronautById(int id) {
        validateId(id, "Astronaut");
        return restUtil.getObject(apiUrl + "/" + id, AstronautDTO.class);
    }

    private void validateId(int id, String entityType) {
        if (id <= 0) {
            throw new IllegalArgumentException(entityType + " ID must be positive");
        }
    }
}