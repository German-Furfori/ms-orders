package com.immfly.msorders.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.immfly.msorders.enums.PaymentStatusEnum;
import lombok.Data;
import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDetailsResponseDto {

    private Long id;

    private Long totalPrice;

    private String cardToken;

    private PaymentStatusEnum status;

    private Instant date;

    private String gateway;

}
