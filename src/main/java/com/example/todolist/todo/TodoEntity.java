package com.example.todolist.todo;


import com.example.todolist.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String task;
    @Column
    private boolean isDone = false;
    @Column
    private LocalDateTime createdTime;
    @Column
    private LocalDate deadline;
    @ManyToOne
    private UserEntity user;
}
