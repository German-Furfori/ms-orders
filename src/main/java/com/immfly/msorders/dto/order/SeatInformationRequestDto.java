package com.immfly.msorders.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SeatInformationRequestDto {

    @NotBlank(message = "The seatLetter field cannot be null or empty")
    @Pattern(regexp = "^[A-Z]$", message = "The seatLetter field must be 1 upper case letter")
    private String seatLetter;

    @NotBlank(message = "The seatNumber field cannot be null or empty")
    @Pattern(regexp = "^[1-9]$", message = "The seatNumber field must be 1 number from 1-9")
    private String seatNumber;

}
