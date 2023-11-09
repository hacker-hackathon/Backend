package com.hacker.hackathon.controller.exception;

import com.hacker.hackathon.common.exception.BaseException;
import com.hacker.hackathon.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class UserConflictException extends BaseException {
    public UserConflictException(ErrorMessage error) {
        super(error, "[UserConflictException] "+ error.getMessage());
    }

}
