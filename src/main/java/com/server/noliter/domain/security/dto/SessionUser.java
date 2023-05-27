package com.server.noliter.domain.security.dto;

import com.server.noliter.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private final Long id;
    private final String username;
    private final String email;
    private final String profileImage;

    public SessionUser(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
    }
}
