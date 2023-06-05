package com.lcwt.electronic.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
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
