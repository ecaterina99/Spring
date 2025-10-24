package com.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetDTO {
    private int id;
    private Integer currentBudget = 1000000;
    private Integer userId;
    private UserDTO userDTO;
}