package com.immfly.msorders.controller;

import com.immfly.msorders.MsOrdersApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class OrderControllerTest extends MsOrdersApplicationTests {

    private final String pathOrders = "/orders";

    private final String pathOrdersProducts = "/products";

    private final String defaultPathOrderId = "/1";

    private static Stream<Arguments> dataForBadRequest() {
        return Stream.of(
                Arguments.of("orders/create-order/createOrderWIthEmptySeatLetter.json", "The seatLetter field must be 1 upper case letter"),
                Arguments.of("orders/createOrderWithEmptySeatNumber.json", "The seatNumber field must be 1 number from 1-9"),
                Arguments.of("orders/createOrderWithIncorrectSeatLetter.json", "The seatLetter field must be 1 upper case letter"),
                Arguments.of("orders/createOrderWithIncorrectSeatLetter2.json", "The seatLetter field must be 1 upper case letter"),
                Arguments.of("orders/createOrderWithIncorrectSeatNumber.json", "The seatNumber field must be 1 number from 1-9"),
                Arguments.of("orders/createOrderWithNullSeatLetter.json", "The seatLetter field cannot be null"),
                Arguments.of("orders/createOrderWithNullSeatNumber.json", "The seatNumber field cannot be null")
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
    @MethodSource("dataForBadRequest")
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
        Long id = this.generateOrderInDatabase();

        mockMvc.perform(delete(pathOrders + "/" + id)
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
    void addProductsToOrder_withFieldsValid_returnOkStatus() {
        this.generateProductInDatabase("Tea");
        this.generateProductInDatabase("Coffee");
        Long id = this.generateOrderInDatabase();

        String bodyRequest = getContentFromFile("orders/add-products/addProductsToOrder.json");

        mockMvc.perform(patch(pathOrders + "/" + id + pathOrdersProducts)
                        .content(bodyRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isNotEmpty())
                .andExpect(jsonPath("$.products[0].id").exists())
                .andExpect(jsonPath("$.products[0].id").value(1))
                .andExpect(jsonPath("$.products[1].id").exists())
                .andExpect(jsonPath("$.products[1].id").value(2))
                .andExpect(jsonPath("$.products[0].name").exists())
                .andExpect(jsonPath("$.products[0].name").value("Tea"))
                .andExpect(jsonPath("$.products[1].name").exists())
                .andExpect(jsonPath("$.products[1].name").value("Coffee"))
                .andExpect(jsonPath("$.products[0].stock").exists())
                .andExpect(jsonPath("$.products[0].stock").value(5))
                .andExpect(jsonPath("$.products[1].stock").exists())
                .andExpect(jsonPath("$.products[1].stock").value(7));
    }

}
