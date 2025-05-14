package com.immfly.msorders.controller;

import com.immfly.msorders.MsOrdersApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends MsOrdersApplicationTests {

    @Test
    @SneakyThrows
    void findAllCategories_withoutData_returnEmptyPage() {
        mockMvc
                .perform(get("/categories/1"))
                .andExpect(status().isUnauthorized());
    }

}
