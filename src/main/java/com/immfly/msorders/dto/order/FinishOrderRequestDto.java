package com.immfly.msorders.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FinishOrderRequestDto {

    @Valid
    @NotNull(message = "The paymentDetails field cannot be null")
    private PaymentDetailsRequestDto paymentDetails;

    @Valid
    @NotNull(message = "The buyerInformation field cannot be null")
    private BuyerInformationRequestDto buyerInformation;

}
