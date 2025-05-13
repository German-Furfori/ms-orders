package com.immfly.msorders.mapper;

import com.immfly.msorders.dto.order.FinishOrderRequestDto;
import com.immfly.msorders.dto.order.OrderResponseDto;
import com.immfly.msorders.dto.order.SeatInformationRequestDto;
import com.immfly.msorders.entity.Order;
import com.immfly.msorders.entity.OrderProduct;
import com.immfly.msorders.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "status", expression = "java(com.immfly.msorders.enums.OrderStatusEnum.OPEN)")
    @Mapping(target = "buyerDetails.seatLetter", source = "seatLetter")
    @Mapping(target = "buyerDetails.seatNumber", source = "seatNumber")
    @Mapping(target = "paymentDetails", expression = "java(new com.immfly.msorders.entity.PaymentDetails())")
    Order seatInformationRequestToOrder(SeatInformationRequestDto seatInformationRequestDto);

    @Mapping(target = "buyerInformation.email", source = "buyerDetails.email")
    @Mapping(target = "seatInformation.seatLetter", source = "buyerDetails.seatLetter")
    @Mapping(target = "seatInformation.seatNumber", source = "buyerDetails.seatNumber")
    OrderResponseDto orderToOrderResponseDto(Order order);

    @Mapping(target = "paymentDetails.id", ignore = true)
    @Mapping(target = "buyerDetails.email", source = "buyerInformation.email")
    void updateOrderWithFinishOrderRequest(FinishOrderRequestDto finishOrderRequestDto,
                             @MappingTarget Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "quantity", source = "quantity")
    OrderProduct getOrderProduct(Order order, Product product, Integer quantity);
}
