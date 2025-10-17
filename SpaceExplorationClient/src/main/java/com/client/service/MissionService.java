package com.client.service;

import com.client.DTO.AstronautDTO;
import com.client.DTO.MissionDTO;
import com.client.exceptions.ApiProxyException;
import com.client.helpers.RestClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MissionService {
    private final String apiUrl;
    private final RestClientUtil restUtil;

    public MissionService(@Value("${api.url}") String apiUrl, RestClientUtil restUtil) {
        this.apiUrl = apiUrl + "/missions";
        this.restUtil = restUtil;
    }

    public MissionDTO startMission(Integer missionId) {
        if (missionId == null || missionId <= 0) {
            throw new IllegalArgumentException("Mission ID must be provided and greater than 0");
        }
        String url = apiUrl + "/" + missionId + "/start" ;
        try {
            return restUtil.postForObject(url, null, MissionDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ApiProxyException(
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString()
            );
        }
    }

    public List<MissionDTO> getMissionsByFilters(
            String difficultyLevel,
            Integer destinationId) {
        StringBuilder url = new StringBuilder(apiUrl);
        List<String> params = new ArrayList<>();

        if (difficultyLevel != null && !difficultyLevel.isEmpty()) {
            params.add("difficultyLevel=" + difficultyLevel);
        }
        if (destinationId != null && destinationId > 0) {
            params.add("destinationId=" + destinationId);
        }
        if (!params.isEmpty()) {
            url.append("?").append(String.join("&", params));
        }
        return restUtil.getList(url.toString(), MissionDTO[].class);
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
