package com.immfly.msorders.service;

import com.immfly.msorders.dto.order.OrderResponseDto;
import com.immfly.msorders.dto.order.SeatInformationRequestDto;

public interface OrderService {

    OrderResponseDto createOrder(SeatInformationRequestDto seatInformationRequestDto);

    OrderResponseDto dropOrder(Long id);

}
