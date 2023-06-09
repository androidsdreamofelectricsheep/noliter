package com.server.noliter.domain.user;

import com.server.noliter.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@DynamicInsert
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(callSuper = false)
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String username, String email, String profileImage, Role role){
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public void updateUser(String username){
        this.username = username;
    }
}
