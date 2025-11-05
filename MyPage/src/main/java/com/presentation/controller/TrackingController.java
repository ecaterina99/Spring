package com.presentation.controller;

import com.presentation.VisitorsConfig.VisitLog;
import com.presentation.VisitorsConfig.VisitLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class TrackingController {

    private final VisitLogRepository repository;

    public TrackingController(VisitLogRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/track-click")
    public ResponseEntity<Void> trackClick(@RequestBody Map<String, String> data,
                                           HttpServletRequest request) {
        String section = data.get("section");
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String url = "http://localhost:8080/home" + section;

        VisitLog log = new VisitLog(ip, userAgent, url, LocalDateTime.now());
        repository.save(log);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/track-external-link")
    public ResponseEntity<Void> trackExternalLink(@RequestBody Map<String, String> data,
                                                  HttpServletRequest request) {
        String url = data.get("url");
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        VisitLog log = new VisitLog(ip, userAgent, "External: " + url, LocalDateTime.now());
        repository.save(log);

        return ResponseEntity.ok().build();
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}