package com.client.DTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionParticipantsDTO {
    private int id;
    private MissionDTO mission;
    private AstronautDTO astronaut;
}
