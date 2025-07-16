package com.link.EmployeesWebApp.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hello")

public class HelloController {

    @GetMapping("/hi")//endpoint specific calea
    public ResponseEntity<String> hello() {
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add("nr_inmatriculare=B-123-ABC");
        cookies.add("sofer=Vasilica; Path=/");
        cookies.add("tara=Romania; Path=/");
        headers.put("Set-Cookie", cookies);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Hello!", headers, HttpStatus.CREATED);

        return responseEntity;
    }

    @GetMapping("/bye/{persoana}/{year}")
    public String goodbye(
            @PathVariable String persoana,
            @PathVariable int year,
            @RequestParam(defaultValue = "") String city, // request param == query param
            @RequestParam(defaultValue = "-1") int km,
            @RequestHeader("Cookie") String cookie
    ) {

        StringBuilder sb = new StringBuilder();
        sb.append("Goodbye, ")
                .append(persoana)
                .append("! Anul este ")
                .append(year)
                .append(" Esti din orasul ")
                .append(city);
        if (km != -1) {
            sb.append(" care se afla la ")
                    .append(km)
                    .append(" km");
        }
        sb.append(" Cookie-ul este: ").append(cookie);

        return sb.toString();
    }

}

