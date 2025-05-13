package com.immfly.msorders.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentDetailsRequestDto {

    @NotBlank(message = "The cardToken field cannot be empty or null")
    private String cardToken;

    @NotBlank(message = "The gateway field cannot be empty or null")
    private String gateway;

}
