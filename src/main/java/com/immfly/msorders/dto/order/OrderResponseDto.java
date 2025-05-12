package com.immfly.msorders.dto.order;

import com.immfly.msorders.dto.product.ProductResponseDto;
import com.immfly.msorders.enums.OrderStatusEnum;
import lombok.Data;
import java.util.List;

@Data
public class OrderResponseDto {

    private Long id;

    private List<ProductResponseDto> products;

    private PaymentDetailsDto paymentDetails;

    private BuyerInformationRequestDto buyerInformation;

    private SeatInformationRequestDto seatInformation;

    private OrderStatusEnum status;

}
