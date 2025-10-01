package com.client.DTO;

import com.client.enums.MissionSpecializationsEnums;

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
