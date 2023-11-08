package com.hacker.hackathon.common.exception;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.controller.exception.BadRequestException;
import com.hacker.hackathon.controller.exception.TokenForbiddenException;
import com.hacker.hackathon.controller.exception.UnauthorizedException;
import com.hacker.hackathon.controller.exception.UserConflictException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    /**
     * 400 BAD REQUEST
     */
    @ExceptionHandler({
            BadRequestException.class
    })
    public ResponseEntity<ApiResponse> BadRequestException(BaseException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }

    @ExceptionHandler({
            ChangeSetPersister.NotFoundException.class
    })
    public ResponseEntity<ApiResponse> NotFoundException(BaseException exception) {
        return ResponseEntity.status(NOT_FOUND).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }

    /**
     * 401 UNAUTHORIZED
     */
    @ExceptionHandler({
        UnauthorizedException.class
    })
    public ResponseEntity<ApiResponse> UnauthorizedException(BaseException exception) {
        return ResponseEntity.status(UNAUTHORIZED)
            .body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }

    /**
     * 403 FORBIDDEN
     */
    @ExceptionHandler({
        TokenForbiddenException.class
    })
    public ResponseEntity<ApiResponse> ForbiddenException(BaseException exception) {
        return ResponseEntity.status(FORBIDDEN)
            .body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }


    /**
     * 404 NOT FOUND
     */


    /**
     * 409 CONFLICT
     */
    @ExceptionHandler({
        UserConflictException.class
    })
    public ResponseEntity<ApiResponse> ConflictException(BaseException exception) {
        return ResponseEntity.status(CONFLICT).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }


    /**
     * 500 INTERNAL SERVER ERROR
     */

}
