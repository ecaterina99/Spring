package com.client.service;

import com.client.DTO.AstronautDTO;
import com.client.helpers.RestClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

}