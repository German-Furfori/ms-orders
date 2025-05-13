package com.immfly.msorders.dto.order;

import com.immfly.msorders.dto.product.ProductRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class FinishOrderRequestDto {

    @Valid
    @NotEmpty(message = "The productList field cannot be empty or null")
    private List<ProductRequestDto> productList;

    @Valid
    @NotNull(message = "The paymentDetails field cannot be null")
    private PaymentDetailsRequestDto paymentDetails;

    @Valid
    @NotNull(message = "The buyerInformation field cannot be null")
    private BuyerInformationRequestDto buyerInformation;

}
