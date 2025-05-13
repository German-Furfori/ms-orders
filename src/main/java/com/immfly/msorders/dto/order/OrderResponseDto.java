package com.immfly.msorders.dto.order;

import com.immfly.msorders.enums.OrderStatusEnum;
import lombok.Data;
import java.util.List;

@Data
public class OrderResponseDto {

    private Long id;

    private List<OrderProductResponseDto> orderProducts;

    private PaymentDetailsResponseDto paymentDetails;

    private BuyerInformationDto buyerInformation;

    private SeatInformationDto seatInformation;

    private OrderStatusEnum status;

}
