package com.github.nk.klusterfuck.admin.controllers.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by nipunkumar on 12/07/17.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
