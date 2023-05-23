package com.server.noliter.domain.reply.exception;

import com.server.noliter.domain.post.exception.PostErrorCode;
import com.server.noliter.global.exception.NoliterException;

public class ReplyException extends NoliterException {
    public ReplyException(ReplyErrorCode code){
        super(code);
    }
}
