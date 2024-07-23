package com.example.todolist.todo;

import com.example.todolist.user.UserDto;
import com.example.todolist.user.UserEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class TodoDto {
    private Long id;
    private String task;
    private Boolean isDone = false;
    private LocalDateTime createdTime = LocalDateTime.now();
    private LocalDate deadline;
    private UserEntity user;


    public TodoDto(String task, LocalDate deadline, UserDto user){
        this.task = task;
        this.deadline = deadline;
        this.user = user.toEntity();
    }

    public TodoEntity toEntity(){
        TodoEntity todoEntity = new TodoEntity(id, task, isDone, createdTime,deadline,user);
        todoEntity.setTask(this.task);
        todoEntity.setCreatedTime(LocalDateTime.now());
        if(deadline != null)
            todoEntity.setDeadline(deadline);
        todoEntity.setUser(user);
        return todoEntity;
    }

}