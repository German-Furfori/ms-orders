package com.immfly.msorders.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatusEnum {

    PAID,

    PAYMENT_FAILED,

    OFFLINE_PAYMENT

}
