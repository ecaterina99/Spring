package com.client.service;

import com.client.DTO.MissionParticipantsDTO;
import com.client.exceptions.ApiProxyException;
import com.client.helpers.RestClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@Slf4j
public class MissionParticipantsService {

    private final String apiUrl;
    private final RestClientUtil restUtil;

    public MissionParticipantsService(@Value("${api.url}") String apiUrl, RestClientUtil restUtil) {
        this.apiUrl = apiUrl + "/mission-participants";
        this.restUtil = restUtil;
    }

    public List<MissionParticipantsDTO> getAllParticipants(Integer missionId) {
        if (missionId == null || missionId <= 0) {
            throw new IllegalArgumentException("Mission ID must be provided and greater than 0");
        }

        String url = apiUrl + "/" + missionId;
        return restUtil.getList(url, MissionParticipantsDTO[].class);
    }

    public MissionParticipantsDTO addParticipants(Integer missionId, Integer participantId) {
        if (missionId == null || missionId <= 0) {
            throw new IllegalArgumentException("Mission ID must be provided and greater than 0");
        }
        if (participantId == null || participantId <= 0) {
            throw new IllegalArgumentException("Participant ID must be provided and greater than 0");
        }

        String url = apiUrl + "/add/" + missionId + "/" + participantId;
        try {
            return restUtil.postForObject(url, null, MissionParticipantsDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ApiProxyException(
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString()
            );
        }
    }

    

}
