package com.immfly.msorders.service.impl;

import com.immfly.msorders.dto.order.OrderResponseDto;
import com.immfly.msorders.dto.order.SeatInformationRequestDto;
import com.immfly.msorders.entity.Order;
import com.immfly.msorders.enums.OrderStatusEnum;
import com.immfly.msorders.exception.DatabaseException;
import com.immfly.msorders.mapper.OrderMapper;
import com.immfly.msorders.repository.OrderRepository;
import com.immfly.msorders.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private static final String NON_EXISTING_ORDER = "Order with ID %s not found";

    @Override
    public OrderResponseDto createOrder(SeatInformationRequestDto seatInformationRequestDto) {
        Order newOrder = orderMapper.seatInformationRequestToOrder(seatInformationRequestDto);
        log.debug("[OrderService] Saving order...");
        newOrder = this.saveOrderOnDataBase(newOrder);
        return orderMapper.orderToOrderResponseDto(newOrder);
    }

    @Override
    public OrderResponseDto dropOrder(Long id) {
        Order orderToDrop = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format(NON_EXISTING_ORDER, id)));
        orderToDrop.setStatus(OrderStatusEnum.DROPPED);
        log.debug("[OrderService] Dropping order...");
        orderToDrop = this.saveOrderOnDataBase(orderToDrop);

        return orderMapper.orderToOrderResponseDto(orderToDrop);
    }

    private Order saveOrderOnDataBase(Order order) {
        try {
            return orderRepository.saveAndFlush(order);
        } catch (Exception ex) {
            log.debug("[OrderService] Something went wrong, error: " + ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
    }
}
