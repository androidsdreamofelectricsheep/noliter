package com.server.noliter.service.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {
    private String username;
    @Builder
    public UserRequest(String username){
        this.username = username;
    }
}
