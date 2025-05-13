package com.immfly.msorders.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatInformationResponseDto {

    private String seatLetter;

    private String seatNumber;

}
