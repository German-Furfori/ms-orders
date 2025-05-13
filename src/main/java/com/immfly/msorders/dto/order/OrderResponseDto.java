package com.immfly.msorders.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.immfly.msorders.enums.OrderStatusEnum;
import lombok.Data;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {

    private Long id;

    private List<OrderProductResponseDto> orderProducts;

    private PaymentDetailsResponseDto paymentDetails;

    private BuyerInformationResponseDto buyerInformation;

    private SeatInformationResponseDto seatInformation;

    private OrderStatusEnum status;

}
