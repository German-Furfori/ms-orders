package com.immfly.msorders.controller.advice;

import com.immfly.msorders.error.ErrorResponse;
import com.immfly.msorders.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        log.error("[ControllerAdvice] methodArgumentNotValidExceptionHandler, exception: [{}]", exception.getMessage());
        return this.generateError(HttpStatus.BAD_REQUEST,
                Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(DatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse databaseExceptionHandler(DatabaseException exception) {
        log.error("[ControllerAdvice] databaseExceptionHandler, exception: [{}]", exception.getMessage());
        return this.generateError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ErrorResponse generateError(HttpStatus httpStatus, String description) {
        return new ErrorResponse(httpStatus.toString(), description);
    }

}
