package com.hacker.hackathon.controller.exception;

import com.hacker.hackathon.common.exception.BaseException;
import com.hacker.hackathon.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class TokenForbiddenException extends BaseException {
    public TokenForbiddenException(ErrorMessage error) {
        super(error, "[TokenForbiddenException] "+ error.getMessage());
    }

}
