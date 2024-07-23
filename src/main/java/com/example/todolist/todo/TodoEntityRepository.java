package com.example.todolist.todo;

import com.example.todolist.user.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoEntityRepository extends JpaRepository<TodoEntity, Long>{
    @Query("SELECT t FROM TodoEntity t WHERE t.user = :user")
    List<TodoEntity> findByUser(@Param("user") UserEntity user, Sort sort);
    @Query("SELECT t FROM TodoEntity t WHERE t.user = :user")
    List<TodoEntity> findByUser(@Param("user") UserEntity user);
}