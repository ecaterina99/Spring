package com.client.DTO;

import com.client.enums.MissionEnums;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionDTO {
    private int id;
    private String name;
    private String code;
    private String description;
    private Integer durationDays;
    private Integer crewSize;
    private Integer scoreValue;
    private String potentialIssues;
    private String destinationName;
    private Integer destinationId;
    private Integer paymentAmount;
    private MissionEnums.DifficultyLevel difficultyLevel;
    private List<MissionSpecializationsDTO> specializations = new ArrayList<>();
    @Setter
    @Getter
    private String imageUrl;

    public String getFullImageUrl() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return "https://ui-avatars.com/api/?name=Mission&background=random";
        }
        return String.format("https://cosmo-trail.s3.eu-central-1.amazonaws.com/%s", imageUrl);
    }

    public String getShortDescription() {
        if (description == null) return "";
        String[] words = description.split("\\s+");
        if (words.length <= 30) {
            return description;
        }
        return String.join(" ", Arrays.copyOfRange(words, 0, 30)) + "...";
    }

}

