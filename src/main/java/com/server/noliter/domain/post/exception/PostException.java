package com.server.noliter.domain.post.exception;

import com.server.noliter.global.exception.NoliterException;

public class PostException extends NoliterException {
    public PostException(PostErrorCode code){
        super(code);
    }
}
