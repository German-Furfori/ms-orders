package com.immfly.msorders.entity;

import com.immfly.msorders.enums.PaymentStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;

    private String cardToken;

    private PaymentStatusEnum status;

    private Instant date;

    private String gateway;

}
