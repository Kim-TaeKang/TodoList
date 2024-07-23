package com.example.todolist.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.User_id = :userId")
    Iterable<UserEntity> findByUserId(@Param("userId") String userId);
    @Query("select u from UserEntity u where u.User_num=:userNum")
    UserEntity findByNum(@Param("userNum") Long userNum);
}