package com.zerobear.UserService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Column(nullable = false, unique = true)
    String eventId;
    @Id
    String userId;
    String name;
    String email;

}
