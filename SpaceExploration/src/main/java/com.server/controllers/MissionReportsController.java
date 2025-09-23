package com.server.controllers;

import com.server.dto.MissionReportDTO;
import com.server.services.MissionReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mission-reports")
@Tag(name = "Mission report", description = "Data about mission results")
public class MissionReportsController {

    private final MissionReportService missionReportService;
    public MissionReportsController(MissionReportService missionReportService) {
        this.missionReportService = missionReportService;
    }
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve mission report by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission report found"),
            @ApiResponse(responseCode = "404", description = "Mission report not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<MissionReportDTO> getMissionReport(@PathVariable int id) {
        MissionReportDTO missionReport = missionReportService.getMissionReportById(id);
        return ResponseEntity.ok(missionReport);
    }

    @GetMapping()
    @Operation(summary = "Retrieve all mission reports")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all mission reports"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<MissionReportDTO>> getAllMissionReports() {
        return ResponseEntity.ok(missionReportService.getAllMissionReports());
    }

   /* @GetMapping("details/{id}")
    @Operation(summary = "Retrieve mission report with mission participants data by mission ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission report found"),
            @ApiResponse(responseCode = "404", description = "Mission report not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<MissionReportDTO> getMissionReportByMissionId(@PathVariable int id) {
        MissionReportDTO missionReport = missionReportService.getMissionReportByMissionIdWithDetails(id);
        return ResponseEntity.ok(missionReport);
    }

    */
}
