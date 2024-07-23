package com.example.todolist.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long User_num;

    @Column(nullable = false)
    private String User_name;

    @Column(nullable = false)
    private String User_id;

    @Column(nullable = false)
    private String User_password;

    @Column
    private String User_mail;

    @Enumerated(EnumType.STRING)
    private Gender User_gender;

    @Enumerated(EnumType.STRING)
    private Role User_role;
}