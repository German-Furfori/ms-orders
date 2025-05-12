package com.immfly.msorders.dto.order;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class BuyerInformationRequestDto {

    @NotBlank
    private String email;

}
