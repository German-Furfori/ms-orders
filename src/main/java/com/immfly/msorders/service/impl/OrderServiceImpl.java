package com.immfly.msorders.service.impl;

import com.immfly.msorders.dto.order.OrderResponseDto;
import com.immfly.msorders.dto.order.ProductListRequestDto;
import com.immfly.msorders.dto.order.SeatInformationRequestDto;
import com.immfly.msorders.entity.Order;
import com.immfly.msorders.entity.Product;
import com.immfly.msorders.enums.OrderStatusEnum;
import com.immfly.msorders.exception.DatabaseException;
import com.immfly.msorders.exception.StockException;
import com.immfly.msorders.mapper.OrderMapper;
import com.immfly.msorders.repository.OrderRepository;
import com.immfly.msorders.repository.ProductRepository;
import com.immfly.msorders.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final OrderMapper orderMapper;

    private static final String NON_EXISTING_ORDER = "Order with ID %s not found";

    private static final String NON_EXISTING_PRODUCT = "Product with ID %s not found";

    private static final String OUT_OF_STOCK_PRODUCT = "Product %s is out of stock";

    private static final String NOT_ENOUGH_STOCK_PRODUCT = "Not enough stock for product %s, stock: %s";

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
        orderToDrop.setStatus(OrderStatusEnum.DROPPED);
        log.debug("[OrderService] Dropping order...");
        orderToDrop = this.saveOrderOnDataBase(orderToDrop);

        return orderMapper.orderToOrderResponseDto(orderToDrop);
    }

    @Override
    public OrderResponseDto addProductsToOrder(Long id, ProductListRequestDto productListRequestDto) {
        Order orderToAddProducts = this.getOrderFromDataBase(id);
        productListRequestDto.getProductList()
                .forEach(productRequest -> {
                    Product product = productRepository.findById(productRequest.getId())
                            .orElseThrow(() -> new NoSuchElementException(String.format(NON_EXISTING_PRODUCT, id)));
                    this.updateProductStockAndOrder(product, productRequest.getQuantity(), orderToAddProducts);
                });
        Order orderSavedWithProducts = this.saveOrderOnDataBase(orderToAddProducts);

        return orderMapper.orderToOrderResponseDto(orderSavedWithProducts);
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

    private void updateProductStockAndOrder(Product product, Integer quantity, Order order) {
        if (order.getProducts() == null) {
            order.setProducts(new ArrayList<>());
        }

        boolean alreadyInOrder = order.getProducts()
                .stream()
                .anyMatch(p -> p.getId().equals(product.getId()));

        if (product.getStock() == 0) {
            throw new StockException(String.format(OUT_OF_STOCK_PRODUCT, product.getName()));
        } else if (product.getStock() - quantity < 0) {
            throw new StockException(String.format(NOT_ENOUGH_STOCK_PRODUCT, product.getName(), product.getStock()));
        } else if (alreadyInOrder) {
            product.setStock(product.getStock() - quantity);
        } else {
            product.setStock(product.getStock() - quantity);
            order.getProducts().add(product);
        }
    }
}
