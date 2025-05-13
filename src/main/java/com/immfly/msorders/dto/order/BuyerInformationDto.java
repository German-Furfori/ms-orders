package com.immfly.msorders.dto.order;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class BuyerInformationDto {

    @NotBlank(message = "The email field cannot be empty or null")
    private String email;

}
