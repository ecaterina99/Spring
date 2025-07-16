package com.link.EmployeesWebApp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDTO {
    boolean success;
    String message;
    Object data;
}
