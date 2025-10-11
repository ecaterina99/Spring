package com.client.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenStorage {
    private String token;

    public void clear() {
        this.token = null;
    }
}