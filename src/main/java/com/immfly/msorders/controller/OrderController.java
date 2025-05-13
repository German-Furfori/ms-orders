package com.immfly.msorders.controller;

import com.immfly.msorders.dto.order.FinishOrderRequestDto;
import com.immfly.msorders.dto.order.OrderResponseDto;
import com.immfly.msorders.dto.order.SeatInformationRequestDto;
import com.immfly.msorders.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    public OrderResponseDto createOrder(@Valid @RequestBody SeatInformationRequestDto seatInformationRequestDto) {
        log.debug("[Orders] createOrder request: [{}]", seatInformationRequestDto);
        OrderResponseDto orderResponseDto = orderService.createOrder(seatInformationRequestDto);
        log.debug("[Orders] createOrder response: [{}]", orderResponseDto);
        return orderResponseDto;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public OrderResponseDto dropOrder(@PathVariable Long id) {
        log.debug("[Orders] dropOrder request: [{}]", id);
        OrderResponseDto orderResponseDto = orderService.dropOrder(id);
        log.debug("[Orders] dropOrder response: [{}]", orderResponseDto);
        return orderResponseDto;
    }

    @PatchMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public OrderResponseDto finishOrder(@PathVariable Long id,
                                        @Valid @RequestBody FinishOrderRequestDto finishOrderRequestDto) {
        log.debug("[Orders] finishOrder request: [{}], [{}]", id, finishOrderRequestDto);
        OrderResponseDto orderResponseDto = orderService.finishOrder(id, finishOrderRequestDto);
        log.debug("[Orders] finishOrder response: [{}]", orderResponseDto);
        return orderResponseDto;
    }

}
