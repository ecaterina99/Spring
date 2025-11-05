package com.presentation.VisitorsConfig;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VisitLogService {
    private final VisitLogRepository repository;

    public VisitLogService(VisitLogRepository repository) {
        this.repository = repository;
    }

    public void logVisit(HttpServletRequest request) {
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURL().toString();

        String queryString = request.getQueryString();
        if (queryString != null) {
            url += "?" + queryString;
        }

        VisitLog log = new VisitLog(ip, userAgent, url, LocalDateTime.now());
        repository.save(log);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}