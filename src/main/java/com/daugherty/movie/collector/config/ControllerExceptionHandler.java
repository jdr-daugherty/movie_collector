package com.daugherty.movie.collector.config;

import com.daugherty.movie.collector.exception.MovieNotFoundException;
import com.daugherty.movie.collector.exception.ReviewNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String movieNotFoundHandler(MovieNotFoundException ex, WebRequest request) {
        log(ex, request);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String reviewNotFoundHandler(ReviewNotFoundException ex, WebRequest request) {
        log(ex, request);
        return ex.getMessage();
    }

    private void log(Exception ex, WebRequest request) {
        String message = ex.getLocalizedMessage();
        if( message == null ) {
            message = ex.toString();
        }
        log.error(String.format("%s (%s)", message, request.getDescription(true)));
    }
}
