package com.server.controllers;

import com.server.dto.DestinationDTO;
import com.server.dto.MissionReportDTO;
import com.server.services.MissionReportService;
import com.server.services.PdfGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mission-reports")
@Tag(name = "Mission report", description = "Data about mission results")
public class MissionReportsController {

    private final MissionReportService missionReportService;
    private final PdfGeneratorService pdfGeneratorService;

    public MissionReportsController(MissionReportService missionReportService, PdfGeneratorService pdfGeneratorService) {
        this.missionReportService = missionReportService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve mission report by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission report found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DestinationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Mission report not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<MissionReportDTO> getMissionReport(@PathVariable int id) {
        MissionReportDTO missionReport = missionReportService.getMissionReportById(id);
        return ResponseEntity.ok(missionReport);
    }

    @GetMapping()
    @Operation(summary = "Retrieve all mission reports")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all mission reports",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DestinationDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<MissionReportDTO>> getAllMissionReports() {
        return ResponseEntity.ok(missionReportService.getAllMissionReports());
    }

    @PostMapping("/export/pdf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<byte[]> exportMissionReport(@RequestBody MissionReportDTO report) {
        try {
            byte[] pdfBytes = pdfGeneratorService.generateMissionReportPdf(report);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename",
                    "mission-report-" + System.currentTimeMillis() + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
