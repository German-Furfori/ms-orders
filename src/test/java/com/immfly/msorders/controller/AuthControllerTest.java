package com.immfly.msorders.controller;

import com.immfly.msorders.MsOrdersApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends MsOrdersApplicationTests {

    private final String pathCategory = "/categories";

    private final String pathOrders = "/orders";

    private final String pathProduct = "/products";

    private final String defaultPathId = "/1";

    private final String defaultPage = "0";

    private final String defaultSize = "2";

    private final String defaultBody = "{}";

    @Test
    @SneakyThrows
    void findAllCategories_withoutCredentials_returnUnauthorized() {
        mockMvc.perform(get(pathCategory)
                        .param("page", defaultPage)
                        .param("size", defaultSize))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void findCategoryById_withoutCredentials_returnUnauthorized() {
        mockMvc.perform(get(pathCategory + defaultPathId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void findAllProducts_withoutCredentials_returnUnauthorized() {
        mockMvc.perform(get(pathProduct)
                        .param("page", defaultPage)
                        .param("size", defaultSize))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void findProductById_withoutCredentials_returnUnauthorized() {
        mockMvc.perform(get(pathProduct + defaultPathId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void createOrder_withoutCredentials_returnUnauthorized() {
        mockMvc.perform(post(pathOrders)
                        .content(defaultBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void dropOrder_withoutCredentials_returnUnauthorized() {
        mockMvc.perform(delete(pathOrders + defaultPathId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void finishOrder_withoutCredentials_returnUnauthorized() {
        mockMvc.perform(patch(pathOrders + defaultPathId)
                        .content(defaultBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

}
