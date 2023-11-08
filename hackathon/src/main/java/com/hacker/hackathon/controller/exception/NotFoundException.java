package com.hacker.hackathon.controller.exception;

import com.hacker.hackathon.common.exception.BaseException;
import com.hacker.hackathon.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {
    public NotFoundException(ErrorMessage error) {
        super(error, "[NotFoundException] "+ error.getMessage());
    }

}
