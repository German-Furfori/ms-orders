package com.immfly.msorders.controller;

import com.immfly.msorders.MsOrdersApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class OrderControllerTest extends MsOrdersApplicationTests {

    private final String pathOrders = "/orders";

    @Test
    @SneakyThrows
    void save_withFieldsValid_returnCreatedStatus() {
        String bodyRequest = getContentFromFile("orders/saveOrder.json");

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

}
