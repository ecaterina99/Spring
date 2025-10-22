package com.client.service;

import com.client.DTO.DestinationDTO;
import com.client.helpers.RestClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.Destination;
import java.util.List;

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

        validateId(id, "Destination");
        return restUtil.getObject(apiUrl  + "/missions/"+ id, DestinationDTO.class );
    }

    private void validateId(int id, String entityType) {
        if (id <= 0) {
            throw new IllegalArgumentException(entityType + " ID must be positive");
        }
    }
}
