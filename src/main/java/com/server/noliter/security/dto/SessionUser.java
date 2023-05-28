package com.server.noliter.security.dto;

import com.server.noliter.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionUser implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String profileImage;

    public SessionUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
    }
}
