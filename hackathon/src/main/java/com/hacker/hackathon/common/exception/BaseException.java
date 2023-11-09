package com.hacker.hackathon.common.exception;

import com.hacker.hackathon.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    private final ErrorMessage error;

    public BaseException(ErrorMessage error, String message) {
        super(message);
        this.error = error;
    }

    public int getHttpStatus() {
        return error.getHttpStatusCode();
    }
}
