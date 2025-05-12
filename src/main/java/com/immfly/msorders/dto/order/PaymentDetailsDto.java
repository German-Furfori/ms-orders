package com.immfly.msorders.dto.order;

import com.immfly.msorders.enums.PaymentStatusEnum;
import lombok.Data;

import java.time.Instant;

@Data
public class PaymentDetailsDto {

    private Long id;

    private Long totalPrice;

    private String cardToken;

    private PaymentStatusEnum status;

    private Instant date;

    private String gateway;

}
