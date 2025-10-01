package com.client.DTO;

import com.client.enums.MissionSpecializationsEnums;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import com.client.enums.MissionEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionSpecializationsDTO {
    private int id;
    private int quantity = 1;
    private MissionDTO mission;
    private MissionSpecializationsEnums.Specialization specialization;
}
