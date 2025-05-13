package com.immfly.msorders.controller;

import com.immfly.msorders.MsOrdersApplicationTests;
import com.immfly.msorders.entity.Order;
import com.immfly.msorders.entity.PaymentDetails;
import com.immfly.msorders.enums.OrderStatusEnum;
import com.immfly.msorders.enums.PaymentStatusEnum;
import com.immfly.msorders.service.PaymentService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class OrderControllerTest extends MsOrdersApplicationTests {

    @MockitoBean
    private PaymentService paymentService;

    private final String pathOrders = "/orders";

    private final String defaultPathOrderId = "/1";

    private static Stream<Arguments> createOrderDataForBadRequest() {
        return Stream.of(
                Arguments.of("orders/create-order/createOrderWIthEmptySeatLetter.json", "The seatLetter field must be 1 upper case letter"),
                Arguments.of("orders/create-order/createOrderWithEmptySeatNumber.json", "The seatNumber field must be 1 number from 1-9"),
                Arguments.of("orders/create-order/createOrderWithIncorrectSeatLetter.json", "The seatLetter field must be 1 upper case letter"),
                Arguments.of("orders/create-order/createOrderWithIncorrectSeatLetter2.json", "The seatLetter field must be 1 upper case letter"),
                Arguments.of("orders/create-order/createOrderWithIncorrectSeatNumber.json", "The seatNumber field must be 1 number from 1-9"),
                Arguments.of("orders/create-order/createOrderWithNullSeatLetter.json", "The seatLetter field cannot be null"),
                Arguments.of("orders/create-order/createOrderWithNullSeatNumber.json", "The seatNumber field cannot be null")
        );
    }

    private static Stream<Arguments> finishOrderDataForBadRequest() {
        return Stream.of(
                Arguments.of("orders/finish-order/finishOrderWithNullList.json", "The productList field cannot be empty or null"),
                Arguments.of("orders/finish-order/finishOrderWithEmptyList.json", "The productList field cannot be empty or null"),
                Arguments.of("orders/finish-order/finishOrderWithNullId.json", "The id field cannot be null"),
                Arguments.of("orders/finish-order/finishOrderWithNullQuantity.json", "The quantity field cannot be null"),
                Arguments.of("orders/finish-order/finishOrderWithZeroId.json", "The id field cannot be 0 or negative"),
                Arguments.of("orders/finish-order/finishOrderWithZeroQuantity.json", "The quantity field cannot be 0 or negative"),
                Arguments.of("orders/finish-order/finishOrderWithNegativeId.json", "The id field cannot be 0 or negative"),
                Arguments.of("orders/finish-order/finishOrderWithNegativeQuantity.json", "The quantity field cannot be 0 or negative"),
                Arguments.of("orders/finish-order/finishOrderWithBlankCardToken.json", "The cardToken field cannot be empty or null"),
                Arguments.of("orders/finish-order/finishOrderWithBlankEmail.json", "The email field cannot be empty or null"),
                Arguments.of("orders/finish-order/finishOrderWithBlankGateway.json", "The gateway field cannot be empty or null"),
                Arguments.of("orders/finish-order/finishOrderWithNullCardToken.json", "The cardToken field cannot be empty or null"),
                Arguments.of("orders/finish-order/finishOrderWithNullEmail.json", "The email field cannot be empty or null"),
                Arguments.of("orders/finish-order/finishOrderWithNullGateway.json", "The gateway field cannot be empty or null"),
                Arguments.of("orders/finish-order/finishOrderWithNullPaymentDetails.json", "The paymentDetails field cannot be null"),
                Arguments.of("orders/finish-order/finishOrderWithNullBuyerInformation.json", "The buyerInformation field cannot be null")
        );
    }

    @Test
    @SneakyThrows
    void createOrder_withFieldsValid_returnCreatedStatus() {
        String bodyRequest = getContentFromFile("orders/create-order/createOrder.json");

        int rowCountOrders = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
        int rowCountBuyerDetails = JdbcTestUtils.countRowsInTable(jdbcTemplate, "buyer_details");

        mockMvc.perform(post(pathOrders)
                        .content(bodyRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatInformation.seatLetter").exists())
                .andExpect(jsonPath("$.seatInformation.seatNumber").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.seatInformation.seatLetter").value("A"))
                .andExpect(jsonPath("$.seatInformation.seatNumber").value("1"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        assertEquals(rowCountOrders + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders"));
        assertEquals(rowCountBuyerDetails + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "buyer_details"));
    }

    @ParameterizedTest
    @MethodSource("createOrderDataForBadRequest")
    @SneakyThrows
    void createOrder_withFieldsInvalid_returnBadRequest(String file, String description) {
        String bodyRequest = getContentFromFile(file);

        mockMvc.perform(post(pathOrders)
                        .content(bodyRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    @SneakyThrows
    void dropOrder_withFieldsValid_returnOkStatus() {
        Order order = this.generateOrderInDatabase(OrderStatusEnum.OPEN);

        mockMvc.perform(delete(pathOrders + "/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.status").value("DROPPED"));
    }

    @Test
    @SneakyThrows
    void dropOrder_withFieldsValid_returnNotFound() {
        mockMvc.perform(delete(pathOrders + defaultPathOrderId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
                .andExpect(jsonPath("$.description").value("Order with ID 1 not found"));
    }

    @Test
    @SneakyThrows
    void dropOrder_withFinishedOrder_returnConflict() {
        Order order = this.generateOrderInDatabase(OrderStatusEnum.FINISHED);

        mockMvc.perform(delete(pathOrders + "/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.code").value("409 CONFLICT"))
                .andExpect(jsonPath("$.description").value("Order with ID 1 has status FINISHED"));
    }

    @Test
    @SneakyThrows
    void finishOrder_withFieldsValid_returnOkStatus() {
        this.generateProductInDatabase("Tea");
        this.generateProductInDatabase("Coffee");
        Order order = this.generateOrderInDatabase(OrderStatusEnum.OPEN);

        given(paymentService.sendPayment(any(PaymentDetails.class)))
                .willReturn(this.getPaymentResponse(order.getPaymentDetails(), PaymentStatusEnum.PAID));

        String bodyRequest = getContentFromFile("orders/finish-order/finishOrder.json");

        mockMvc.perform(patch(pathOrders + "/" + order.getId())
                        .content(bodyRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderProducts").isNotEmpty())
                .andExpect(jsonPath("$.orderProducts[0].quantity").exists())
                .andExpect(jsonPath("$.orderProducts[1].quantity").exists())
                .andExpect(jsonPath("$.orderProducts[0].product.id").exists())
                .andExpect(jsonPath("$.orderProducts[1].product.id").exists())
                .andExpect(jsonPath("$.orderProducts[0].product.name").exists())
                .andExpect(jsonPath("$.orderProducts[1].product.name").exists())
                .andExpect(jsonPath("$.orderProducts[0].product.stock").exists())
                .andExpect(jsonPath("$.orderProducts[1].product.stock").exists())
                .andExpect(jsonPath("$.paymentDetails.totalPrice").exists())
                .andExpect(jsonPath("$.paymentDetails.cardToken").exists())
                .andExpect(jsonPath("$.paymentDetails.status").exists())
                .andExpect(jsonPath("$.paymentDetails.date").exists())
                .andExpect(jsonPath("$.paymentDetails.gateway").exists())
                .andExpect(jsonPath("$.buyerInformation.email").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.orderProducts[0].quantity").value(5))
                .andExpect(jsonPath("$.orderProducts[1].quantity").value(5))
                .andExpect(jsonPath("$.orderProducts[0].product.id").value(1))
                .andExpect(jsonPath("$.orderProducts[1].product.id").value(2))
                .andExpect(jsonPath("$.orderProducts[0].product.name").value("Tea"))
                .andExpect(jsonPath("$.orderProducts[1].product.name").value("Coffee"))
                .andExpect(jsonPath("$.orderProducts[0].product.stock").value(5))
                .andExpect(jsonPath("$.orderProducts[1].product.stock").value(5))
                .andExpect(jsonPath("$.paymentDetails.totalPrice").value(100))
                .andExpect(jsonPath("$.paymentDetails.cardToken").value("token"))
                .andExpect(jsonPath("$.paymentDetails.status").value("PAID"))
                .andExpect(jsonPath("$.paymentDetails.gateway").value("gateway"))
                .andExpect(jsonPath("$.buyerInformation.email").value("name@example.com"))
                .andExpect(jsonPath("$.status").value("FINISHED"));

        verify(paymentService, times(1)).sendPayment(any(PaymentDetails.class));
    }

    @ParameterizedTest
    @MethodSource("finishOrderDataForBadRequest")
    @SneakyThrows
    void finishOrder_withFieldsInvalid_returnBadRequest(String file, String description) {
        String bodyRequest = getContentFromFile(file);

        mockMvc.perform(patch(pathOrders + defaultPathOrderId)
                        .content(bodyRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    @SneakyThrows
    void finishOrder_withDroppedOrder_returnConflict() {
        Order order = this.generateOrderInDatabase(OrderStatusEnum.DROPPED);

        String bodyRequest = getContentFromFile("orders/finish-order/finishOrder.json");

        mockMvc.perform(patch(pathOrders + "/" + order.getId())
                        .content(bodyRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.code").value("409 CONFLICT"))
                .andExpect(jsonPath("$.description").value("Order with ID 1 has status DROPPED"));
    }

    @Test
    @SneakyThrows
    void finishOrder_twoTimes_returnOk() {
        this.generateProductInDatabase("Tea");
        this.generateProductInDatabase("Coffee");
        Order order = this.generateOrderInDatabase(OrderStatusEnum.OPEN);

        int rowCountOrderProductsBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders_products");

        given(paymentService.sendPayment(any(PaymentDetails.class)))
                .willReturn(this.getPaymentResponse(order.getPaymentDetails(), PaymentStatusEnum.PAYMENT_FAILED),
                        this.getPaymentResponse(order.getPaymentDetails(), PaymentStatusEnum.PAYMENT_FAILED));

        String bodyRequest = getContentFromFile("orders/finish-order/finishOrder.json");

        mockMvc.perform(patch(pathOrders + "/" + order.getId())
                        .content(bodyRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        int rowCountOrderProductsAfter1Call = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders_products");

        mockMvc.perform(patch(pathOrders + "/" + order.getId())
                        .content(bodyRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        int rowCountOrderProductsAfter2Calls = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders_products");

        assertEquals(rowCountOrderProductsBefore, 0);
        assertEquals(rowCountOrderProductsAfter1Call, 2);
        assertEquals(rowCountOrderProductsAfter2Calls, 2);
    }

}