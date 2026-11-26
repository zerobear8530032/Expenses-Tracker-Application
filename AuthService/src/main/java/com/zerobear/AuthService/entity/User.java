package com.zerobear.AuthService.entity;

import com.zerobear.AuthService.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_user")
public class User {

    @Id
    @Column(name = "user_id")
    private String id;
    @Column(unique = true)
    private String userName;
    private String password;// hashed passwor
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

}
