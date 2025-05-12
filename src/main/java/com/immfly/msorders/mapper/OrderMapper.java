package com.immfly.msorders.mapper;

import com.immfly.msorders.dto.order.OrderResponseDto;
import com.immfly.msorders.dto.order.SeatInformationRequestDto;
import com.immfly.msorders.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "status", expression = "java(com.immfly.msorders.enums.OrderStatusEnum.OPEN)")
    @Mapping(target = "buyerDetails.seatLetter", source = "seatLetter")
    @Mapping(target = "buyerDetails.seatNumber", source = "seatNumber")
    Order seatInformationRequestToOrder(SeatInformationRequestDto seatInformationRequestDto);

    @Mapping(target = "buyerInformation.email", source = "buyerDetails.email")
    @Mapping(target = "seatInformation.seatLetter", source = "buyerDetails.seatLetter")
    @Mapping(target = "seatInformation.seatNumber", source = "buyerDetails.seatNumber")
    OrderResponseDto orderToOrderResponseDto(Order order);

}
