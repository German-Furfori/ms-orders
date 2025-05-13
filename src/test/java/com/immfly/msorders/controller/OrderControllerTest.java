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

    private static Stream<Arguments> addProductsDataForBadRequest() {
        return Stream.of(
                Arguments.of("orders/add-products/addProductsToOrderWithNullList.json", "The productList field cannot be empty or null"),
                Arguments.of("orders/add-products/addProductsToOrderWithEmptyList.json", "The productList field cannot be empty or null"),
                Arguments.of("orders/add-products/addProductsToOrderWithNullId.json", "The id field cannot be null"),
                Arguments.of("orders/add-products/addProductsToOrderWithNullQuantity.json", "The quantity field cannot be null"),
                Arguments.of("orders/add-products/addProductsToOrderWithZeroId.json", "The id field cannot be 0 or negative"),
                Arguments.of("orders/add-products/addProductsToOrderWithZeroQuantity.json", "The quantity field cannot be 0 or negative"),
                Arguments.of("orders/add-products/addProductsToOrderWithNegativeId.json", "The id field cannot be 0 or negative"),
                Arguments.of("orders/add-products/addProductsToOrderWithNegativeQuantity.json", "The quantity field cannot be 0 or negative")
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
                .andExpect(jsonPath("$.orderProducts").isNotEmpty())
                .andExpect(jsonPath("$.orderProducts[0].quantity").exists())
                .andExpect(jsonPath("$.orderProducts[0].quantity").value(5))
                .andExpect(jsonPath("$.orderProducts[1].quantity").exists())
                .andExpect(jsonPath("$.orderProducts[1].quantity").value(3))
                .andExpect(jsonPath("$.orderProducts[0].product.id").exists())
                .andExpect(jsonPath("$.orderProducts[0].product.id").value(1))
                .andExpect(jsonPath("$.orderProducts[1].product.id").exists())
                .andExpect(jsonPath("$.orderProducts[1].product.id").value(2))
                .andExpect(jsonPath("$.orderProducts[0].product.name").exists())
                .andExpect(jsonPath("$.orderProducts[0].product.name").value("Tea"))
                .andExpect(jsonPath("$.orderProducts[1].product.name").exists())
                .andExpect(jsonPath("$.orderProducts[1].product.name").value("Coffee"))
                .andExpect(jsonPath("$.orderProducts[0].product.stock").exists())
                .andExpect(jsonPath("$.orderProducts[0].product.stock").value(5))
                .andExpect(jsonPath("$.orderProducts[1].product.stock").exists())
                .andExpect(jsonPath("$.orderProducts[1].product.stock").value(7));
    }

    @ParameterizedTest
    @MethodSource("addProductsDataForBadRequest")
    @SneakyThrows
    void addProductsToOrder_withFieldsInvalid_returnBadRequest(String file, String description) {
        String bodyRequest = getContentFromFile(file);

        mockMvc.perform(patch(pathOrders + defaultPathOrderId + pathOrdersProducts)
                        .content(bodyRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description").value(description));
    }

}
