package com.immfly.msorders.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SeatInformationDto {

    @NotNull(message = "The seatLetter field cannot be null")
    @Pattern(regexp = "^[A-Z]$", message = "The seatLetter field must be 1 upper case letter")
    private String seatLetter;

    @NotNull(message = "The seatNumber field cannot be null")
    @Pattern(regexp = "^[1-9]$", message = "The seatNumber field must be 1 number from 1-9")
    private String seatNumber;

}
