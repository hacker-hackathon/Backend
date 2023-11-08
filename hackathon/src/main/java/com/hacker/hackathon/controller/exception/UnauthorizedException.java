package com.hacker.hackathon.controller.exception;

import com.hacker.hackathon.common.exception.BaseException;
import com.hacker.hackathon.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ErrorMessage error) {
        super(error, "[UnauthorizedException] "+ error.getMessage());
    }

}
