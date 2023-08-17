package com.mapbefine.mapbefine.common;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.mapbefine.mapbefine.common.dto.ErrorResponse;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handle(GlobalException exception, HttpServletRequest request) {
        String exceptionSource = extractExceptionSource(exception);
        ErrorCode errorCode = exception.getErrorCode();

        log.warn(
                "source = {} , {} = {} code = {} message = {}", exceptionSource,
                request.getMethod(), request.getRequestURI(),
                errorCode.getCode(), errorCode.getMessage()
        );

        return ResponseEntity.status(exception.getStatus()).body(ErrorResponse.from(errorCode));
    }

    private String extractExceptionSource(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        if (stackTrace.length > 0) {
            return stackTrace[0].toString();
        }
        return "Unknown Source";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleServerException(Exception exception) {
        log.error("", exception);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    }

}
