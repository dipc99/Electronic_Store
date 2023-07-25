package com.lcwt.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User extends CustomFields{
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_email")
    private String email;
    @Column(name = "user_password")
    private String password;

    private String gender;
    @Column(length = 1000)
    private String about;
    @Column(name = "user_image_name")
    private String image_name;
}
