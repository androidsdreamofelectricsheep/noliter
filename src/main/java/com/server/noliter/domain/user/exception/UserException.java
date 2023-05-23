package com.server.noliter.domain.user.exception;

import com.server.noliter.global.exception.NoliterException;

public class UserException  extends NoliterException {
    public UserException(UserErrorCode code){
        super(code);
    }
}
