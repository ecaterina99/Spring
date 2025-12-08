package com.example.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/events")
public class EventsController {

    private final List<String> eventLog = new ArrayList<>();

    @GetMapping
    public String showEvents(Model model) {
        model.addAttribute("events", eventLog);
        return "events";
    }

    @GetMapping("/data")
    @ResponseBody
    public List<String> getEventsData() {
        return eventLog;
    }

    public void addEvent(String event) {
        if (eventLog.size() > 100) {
            eventLog.remove(0);
        }
        eventLog.add(event);
    }
}
