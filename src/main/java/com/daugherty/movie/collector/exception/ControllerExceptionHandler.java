package com.daugherty.movie.collector.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

import static java.lang.String.format;

@Slf4j
@ControllerAdvice("com.daugherty.movie.collector.controller")
class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(MovieNotFoundException.class)
    private ResponseEntity<String> movieNotFoundHandler(MovieNotFoundException ex, HttpServletRequest request) {
        log(ex, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ReviewNotFoundException.class)
    private ResponseEntity<String> reviewNotFoundHandler(ReviewNotFoundException ex, HttpServletRequest request) {
        log(ex, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    private void log(Exception ex, HttpServletRequest request) {
        String message = ex.getLocalizedMessage();
        if (message == null) {
            message = ex.toString();
        }

        log.error(format("%s (%s => %s)", message, request.getMethod(), request.getRequestURI()));
    }
}
