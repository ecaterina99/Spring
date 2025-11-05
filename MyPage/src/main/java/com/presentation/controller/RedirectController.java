package com.presentation.controller;

import com.presentation.VisitorsConfig.VisitLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RedirectController {

    private final VisitLogService logService;

    public RedirectController(VisitLogService logService) {
        this.logService = logService;
    }

    @GetMapping("/redirect")
    public String redirect(@RequestParam String to, HttpServletRequest request) {
        logService.logVisit(request);
        return "redirect:" + to;
    }
}
