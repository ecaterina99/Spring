package com.presentation.VisitorsConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final VisitLogService visitLogService;

    public WebConfig(VisitLogService visitLogService) {
        this.visitLogService = visitLogService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object handler) {
                if ("GET".equalsIgnoreCase(request.getMethod()) &&
                        shouldLogRequest(request)) {
                    visitLogService.logVisit(request);
                }
                return true;
            }

            private boolean shouldLogRequest(HttpServletRequest request) {
                String uri = request.getRequestURI().toLowerCase();

                if (uri.matches(".*\\.(jpg|jpeg|png|gif|css|js|ico|svg|woff|woff2|ttf|eot|map|webp)$")) {
                    return false;
                }

                if (uri.startsWith("/innerfolder/") ||
                        uri.startsWith("/css/") ||
                        uri.startsWith("/js/") ||
                        uri.startsWith("/images/")) {
                    return false;
                }

                if (uri.equals("/error")) {
                    return false;
                }

                if (uri.equals("/redirect")) {
                    return false;
                }

                return true;
            }
        });
    }
}