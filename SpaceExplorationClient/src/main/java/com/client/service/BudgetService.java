package com.client.service;

import com.client.DTO.BudgetDTO;
import com.client.DTO.MissionDTO;
import com.client.helpers.RestClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BudgetService {
    private final String apiUrl;
    private final RestClientUtil restUtil;


    public BudgetService(@Value("${api.url}") String apiUrl, RestClientUtil restUtil) {
        this.apiUrl = apiUrl + "/budgets";
        this.restUtil = restUtil;
    }


    public BudgetDTO getUserBudget(Integer userId) {
        try {
            return restUtil.getObject(apiUrl + "/" + userId, BudgetDTO.class);
        } catch (Exception e) {
            log.error("Failed to fetch budget for user {}: {}", userId, e.getMessage());
            return null;
        }
    }
}
