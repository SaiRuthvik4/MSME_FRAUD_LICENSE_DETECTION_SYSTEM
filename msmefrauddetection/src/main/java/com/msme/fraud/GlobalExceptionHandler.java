package com.msme.fraud;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔥 Handle all runtime errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("ERROR", ex.getMessage()));
    }

    // 🔥 Handle multipart upload size errors
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErrorResponse("FAIL", "File too large. Max size is 10MB."));
    }

    // 🔥 Handle all general errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("FAIL", ex.getMessage()));
    }

    // ✅ Custom response class
    static class ErrorResponse {
        public String status;
        public String message;

        public ErrorResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}