package com.presentation.VisitorsConfig;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity(name = "visit_logs")
public class VisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ipAddress;
    private String userAgent;
    private String url;
    private LocalDateTime timestamp;

    public VisitLog() {
    }

    public VisitLog(String ipAddress, String userAgent, String url, LocalDateTime timestamp) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.url = url;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
