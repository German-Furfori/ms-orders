package com.immfly.msorders.service.impl;

import com.immfly.msorders.dto.order.FinishOrderRequestDto;
import com.immfly.msorders.dto.order.OrderResponseDto;
import com.immfly.msorders.dto.order.SeatInformationRequestDto;
import com.immfly.msorders.entity.Order;
import com.immfly.msorders.entity.OrderProduct;
import com.immfly.msorders.entity.PaymentDetails;
import com.immfly.msorders.entity.Product;
import com.immfly.msorders.enums.OrderStatusEnum;
import com.immfly.msorders.enums.PaymentStatusEnum;
import com.immfly.msorders.exception.DatabaseException;
import com.immfly.msorders.mapper.OrderMapper;
import com.immfly.msorders.repository.OrderProductRepository;
import com.immfly.msorders.repository.OrderRepository;
import com.immfly.msorders.repository.ProductRepository;
import com.immfly.msorders.service.OrderService;
import com.immfly.msorders.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final OrderProductRepository orderProductRepository;

    private final OrderMapper orderMapper;

    private final PaymentService paymentService;

    private static final String NON_EXISTING_ORDER = "Order with ID %s not found";

    private static final String NON_EXISTING_PRODUCT = "Product with ID %s not found";

    private static final String OUT_OF_STOCK_PRODUCT = "Product %s is out of stock";

    private static final String NOT_ENOUGH_STOCK_PRODUCT = "Not enough stock for product %s, stock: %s";

    private static final String NOT_OPEN_ORDER = "Order with ID %s has status %s";

    @Override
    public OrderResponseDto createOrder(SeatInformationRequestDto seatInformationRequestDto) {
        Order newOrder = orderMapper.seatInformationRequestToOrder(seatInformationRequestDto);
        log.debug("[OrderService] Saving order...");
        newOrder = this.saveOrderOnDataBase(newOrder);
        return orderMapper.orderToOrderResponseDto(newOrder);
    }

    @Override
    public OrderResponseDto dropOrder(Long id) {
        Order orderToDrop = this.getOrderFromDataBase(id);
        this.verifyOrderStatus(orderToDrop);
        orderToDrop.setStatus(OrderStatusEnum.DROPPED);
        log.debug("[OrderService] Dropping order...");
        orderToDrop = this.saveOrderOnDataBase(orderToDrop);

        return orderMapper.orderToOrderResponseDto(orderToDrop);
    }

    @Override
    public OrderResponseDto finishOrder(Long id, FinishOrderRequestDto finishOrderRequestDto) {
        Order orderToFinish = this.getOrderFromDataBase(id);
        this.verifyOrderStatus(orderToFinish);
        this.initializeOrderProductList(orderToFinish);
        orderToFinish = this.addProductsToOrder(orderToFinish, finishOrderRequestDto);
        orderMapper.updateOrderWithFinishOrderRequest(finishOrderRequestDto, orderToFinish);
        orderToFinish = this.saveOrderOnDataBase(orderToFinish);
        PaymentDetails payment = this.paymentService.sendPayment(orderToFinish.getPaymentDetails());
        orderToFinish.getPaymentDetails().setStatus(payment.getStatus());
        if (PaymentStatusEnum.PAID.equals(payment.getStatus())) {
            orderToFinish.setStatus(OrderStatusEnum.FINISHED);
            this.updateProductStocksAfterPayment(orderToFinish);
        }
        orderToFinish = this.saveOrderOnDataBase(orderToFinish);

        return orderMapper.orderToOrderResponseDto(orderToFinish);
    }

    private Order saveOrderOnDataBase(Order order) {
        try {
            return orderRepository.saveAndFlush(order);
        } catch (Exception ex) {
            log.debug("[OrderService] Something went wrong, error: " + ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
    }

    private Order getOrderFromDataBase(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format(NON_EXISTING_ORDER, id)));
    }

    private Order addProductsToOrder(Order order, FinishOrderRequestDto finishOrderRequestDto) {
        finishOrderRequestDto.getProductList()
                .forEach(productRequest -> {
                    Product product = productRepository.findById(productRequest.getId())
                            .orElseThrow(() -> new NoSuchElementException(String.format(NON_EXISTING_PRODUCT, productRequest.getId())));
                    this.updateOrderWithProducts(product, productRequest.getQuantity(), order);
                });

        return this.saveOrderOnDataBase(order);
    }

    private void updateOrderWithProducts(Product product, Integer quantity, Order order) {
        if (product.getStock() == 0) {
            throw new UnsupportedOperationException(String.format(OUT_OF_STOCK_PRODUCT, product.getName()));
        } else if (product.getStock() - quantity < 0) {
            throw new UnsupportedOperationException(String.format(NOT_ENOUGH_STOCK_PRODUCT, product.getName(), product.getStock()));
        } else {
            OrderProduct newOrderProduct = orderMapper.getOrderProduct(order, product, quantity);
            Long totalPrice = order.getPaymentDetails().getTotalPrice() +
                    newOrderProduct.getProduct().getPrice() * newOrderProduct.getQuantity();
            order.getPaymentDetails().setTotalPrice(totalPrice);
            order.getOrderProducts().add(newOrderProduct);
        }
    }

    private void verifyOrderStatus(Order orderToFinish) {
         if (!OrderStatusEnum.OPEN.equals(orderToFinish.getStatus())) {
            throw new UnsupportedOperationException(String.format(NOT_OPEN_ORDER, orderToFinish.getId(), orderToFinish.getStatus()));
        }
    }

    private void initializeOrderProductList(Order orderToFinish) {
        if (orderToFinish.getOrderProducts() != null && !orderToFinish.getOrderProducts().isEmpty()) {
            orderToFinish.getOrderProducts().forEach(orderProduct -> {
                log.debug("Cleaning old data: {}", orderProduct.getId());
                orderProductRepository.deleteByProductOrderId(orderProduct.getId());
            });
        }

        orderToFinish.setOrderProducts(new ArrayList<>());
    }

    private void updateProductStocksAfterPayment(Order orderToFinish) {
        orderToFinish.getOrderProducts()
                .forEach(orderProduct -> {
                    Integer finalStock = orderProduct.getProduct().getStock() - orderProduct.getQuantity();
                    orderProduct.getProduct().setStock(finalStock);
                });
    }
}
