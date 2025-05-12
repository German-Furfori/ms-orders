package com.immfly.msorders.dto.order;

import com.immfly.msorders.dto.product.ProductRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class ProductListRequestDto {

    @Valid
    @NotEmpty(message = "The productList field cannot be empty or null")
    private List<ProductRequestDto> productList;

}
