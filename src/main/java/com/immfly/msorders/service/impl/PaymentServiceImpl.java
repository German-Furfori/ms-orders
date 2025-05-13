package com.immfly.msorders.service.impl;

import com.immfly.msorders.entity.PaymentDetails;
import com.immfly.msorders.enums.PaymentStatusEnum;
import com.immfly.msorders.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final Random random = new Random();

    @Override
    public PaymentDetails sendPayment(PaymentDetails payment) {
        PaymentStatusEnum[] statuses = PaymentStatusEnum.values();
        payment.setStatus(statuses[random.nextInt(statuses.length)]);

        return payment;
    }

}
