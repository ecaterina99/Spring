package com.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Payments details")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private int id;
    private int initialBudget;
    private int proceeds;
    private int finalBudget;
    private int missionPayment;
    private int missionId;
}
