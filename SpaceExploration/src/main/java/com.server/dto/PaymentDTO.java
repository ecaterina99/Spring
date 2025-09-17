package com.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Payments details")
public class PaymentDTO {
    private int id;
    private int initialBudget;
    private int proceeds;
    private int finalBudget;
    private int missionPayment;
    private int missionId;
}
