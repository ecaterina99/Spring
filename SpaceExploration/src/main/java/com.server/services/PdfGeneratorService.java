package com.server.services;

import com.server.dto.MissionParticipantsDTO;
import com.server.dto.MissionReportDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfGeneratorService {

    public byte[] generateMissionReportPdf(MissionReportDTO report) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = 750;
                float margin = 50;

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                String title = report.isSuccessful() ? "MISSION ACCOMPLISHED!" : "MISSION FAILED!";
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(title);
                contentStream.endText();
                yPosition -= 40;

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("MISSION OVERVIEW");
                contentStream.endText();
                yPosition -= 25;

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                yPosition = addLine(contentStream, margin, yPosition,
                        "Mission: " + report.getMissionName());
                yPosition = addLine(contentStream, margin, yPosition,
                        "Destination: " + report.getDestinationName());
                yPosition = addLine(contentStream, margin, yPosition,
                        "Success Rate: " + report.getSuccessChance() + "%");
                yPosition = addLine(contentStream, margin, yPosition,
                        "Crew Size: " + report.getCrewSize());


                if (report.isSuccessful()) {
                    yPosition = addLine(contentStream, margin, yPosition,
                            "Payment for mission: $" + String.format("%,d", report.getPaymentAmount()));
                }
                yPosition = addLine(contentStream, margin, yPosition,
                        "Total Salary Paid: $" + String.format("%,d", report.getTotalSalary()));

                yPosition -= 20;


                if (report.getParticipants() != null && !report.getParticipants().isEmpty()) {
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("CREW ROSTER");
                    contentStream.endText();
                    yPosition -= 25;

                    contentStream.setFont(PDType1Font.HELVETICA, 11);
                    for (MissionParticipantsDTO member : report.getParticipants()) {
                        String astronautName = member.getAstronautName() != null
                                ? member.getAstronautName()
                                : "UNKNOWN";
                        String specialization = member.getSpecialization() != null
                                ? member.getSpecialization().toString().toLowerCase()
                                : "Unknown";

                        yPosition = addLine(contentStream, margin, yPosition,
                                String.format("%s - %s", astronautName, specialization));
                    }
                } else {
                    System.out.println("No participants found in mission report");
                }


                // Failure Analysis

                if (!report.isSuccessful()) {
                    yPosition -= 20;
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("FAILURE ANALYSIS");
                    contentStream.endText();
                    yPosition -= 25;

                    if (report.getAlienAttack() != null && report.getAlienAttack()) {
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
                        yPosition = addLine(contentStream, margin, yPosition,
                                "ALIEN ENCOUNTER DETECTED!");
                        yPosition -= 5;
                    }

                    if (report.getIssues() != null && !report.getIssues().isEmpty()) {
                        contentStream.setFont(PDType1Font.HELVETICA, 11);
                        yPosition = addLine(contentStream, margin, yPosition,
                                "Critical Issues:");
                        for (String issue : report.getIssues()) {
                            yPosition = addLine(contentStream, margin + 10, yPosition,
                                    "- " + issue);
                        }
                    }
                }
            }

            document.save(baos);
            return baos.toByteArray();
        }
    }

    private float addLine(PDPageContentStream contentStream, float x, float y, String text)
            throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        return y - 20;
    }
}