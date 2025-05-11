package com.immfly.msorders.controller;

import com.immfly.msorders.MsOrdersApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class ProductControllerTest extends MsOrdersApplicationTests {

    private final String pathCategory = "/products";

    private final String defaultPage = "0";

    private final String defaultSize = "4";

    @Test
    @SneakyThrows
    void findAllProducts_withData_returnPage() {
        this.generateProductInDatabase("Tea");
        this.generateProductInDatabase("Coffee");
        this.generateProductInDatabase("Sandwich");
        this.generateProductInDatabase("Pizza");
        this.generateProductInDatabase("Potatoes");
        this.generateProductInDatabase("Candy");
        this.generateProductInDatabase("Water");
        this.generateProductInDatabase("Wine");

        mockMvc
                .perform(get(pathCategory)
                        .param("page", defaultPage)
                        .param("size", defaultSize))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.count").value("4"))
                .andExpect(jsonPath("$.total").value("8"))
                .andExpect(jsonPath("$.results").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void findAllProducts_withoutData_returnEmptyPage() {
        mockMvc
                .perform(get(pathCategory)
                        .param("page", defaultPage)
                        .param("size", defaultSize))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.results").isEmpty());
    }

}
