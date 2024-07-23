package com.example.todolist.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    MASTER,
    ADMIN,
    USER
}