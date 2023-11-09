package com.hacker.hackathon.controller.exception;

import com.hacker.hackathon.common.exception.BaseException;
import com.hacker.hackathon.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class BadRequestException extends BaseException {
    public BadRequestException(ErrorMessage error) {
        super(error, "[Exception] "+ error.getMessage());
    }

}
