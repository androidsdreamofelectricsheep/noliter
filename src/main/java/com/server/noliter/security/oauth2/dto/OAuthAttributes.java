package com.server.noliter.security.oauth2.dto;

import com.server.noliter.domain.user.Role;
import com.server.noliter.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;


@Getter
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String username;
    private final String email;
    private final String profileImage;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String username, String email, String profileImage){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
    }

    public static OAuthAttributes of(String registrationId, String usernameAttributeName, Map<String, Object> attributes){
        if("google".equals(registrationId)){
            return ofGoogle(usernameAttributeName, attributes);
        }
        return null;
    }

    private static OAuthAttributes ofGoogle(String usernameAttributeName, Map<String, Object>attributes){
        return OAuthAttributes.builder()
                .username((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImage((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .username(username)
                .email(email)
                .profileImage(profileImage)
                .role(Role.USER)
                .build();
    }

}
