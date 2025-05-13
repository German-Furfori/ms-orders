package com.immfly.msorders.service;

import com.immfly.msorders.entity.PaymentDetails;

public interface PaymentService {

    PaymentDetails sendPayment(PaymentDetails order);

}
