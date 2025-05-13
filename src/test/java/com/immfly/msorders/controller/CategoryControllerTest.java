package com.immfly.msorders.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.immfly.msorders.MsOrdersApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CategoryControllerTest extends MsOrdersApplicationTests {

    private final String pathCategory = "/categories";

    private final String defaultPathCategoryId = "/1";

    private final String defaultPage = "0";

    private final String defaultSize = "2";

    @Test
    @SneakyThrows
    void findAllCategories_withData_returnPage() {
        this.generateCategoryInDatabase("Breakfast");
        this.generateCategoryInDatabase("Lunch/Dinner");
        this.generateCategoryInDatabase("Snacks");
        this.generateCategoryInDatabase("Drinks");

        mockMvc
                .perform(get(pathCategory)
                        .param("page", defaultPage)
                        .param("size", defaultSize))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.count").value("2"))
                .andExpect(jsonPath("$.total").value("4"))
                .andExpect(jsonPath("$.results").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void findAllCategories_withoutData_returnEmptyPage() {
        mockMvc
                .perform(get(pathCategory)
                        .param("page", defaultPage)
                        .param("size", defaultSize))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.results").isEmpty());
    }

    @Test
    @SneakyThrows
    void findCategoryById_withData_returnCategory() {
        this.generateCategoryInDatabase("Breakfast");

        mockMvc
                .perform(get(pathCategory + defaultPathCategoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Breakfast"));
    }

    @Test
    @SneakyThrows
    void findCategoryById_withNoData_returnNotFound() {
        mockMvc
                .perform(get(pathCategory + defaultPathCategoryId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
                .andExpect(jsonPath("$.description").value("Category with ID 1 not found"));
    }

}
