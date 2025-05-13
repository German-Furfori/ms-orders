package com.immfly.msorders.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductForOrderRequestDto {

    @NotNull(message = "The id field cannot be null")
    @Positive(message = "The id field cannot be 0 or negative")
    private Long id;

    @NotNull(message = "The quantity field cannot be null")
    @Positive(message = "The quantity field cannot be 0 or negative")
    private Integer quantity;

}
