package com.client.service;

import com.client.DTO.AstronautDTO;
import com.client.DTO.MissionDTO;
import com.client.helpers.RestClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionService {
    private final String apiUrl;
    private final RestClientUtil restUtil;

    public MissionService(@Value("${api.url}") String apiUrl, RestClientUtil restUtil) {
        this.apiUrl = apiUrl + "/missions";
        this.restUtil = restUtil;
    }

    public List<MissionDTO> getAllMissions() {
        return restUtil.getList(apiUrl, MissionDTO[].class);
    }

    public MissionDTO getMissionById(int id) {
        validateId(id, "Mission");
        return restUtil.getObject(apiUrl + "/" + id, MissionDTO.class);
    }

    private void validateId(int id, String entityType) {
        if (id <= 0) {
            throw new IllegalArgumentException(entityType + " ID must be positive");
        }
    }
}
