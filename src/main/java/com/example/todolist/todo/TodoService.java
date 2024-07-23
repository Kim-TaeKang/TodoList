package com.example.todolist.todo;

import com.example.todolist.user.UserDto;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoEntityRepository todoRepository;

    private final Sort todoSort = Sort.by(Sort.Order.asc("deadline"));

    public TodoService(TodoEntityRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoEntity> findUserTodos(UserDto userDto) {
        return todoRepository
                .findByUser(userDto.toEntity(),todoSort);
    }

    public List<TodoEntity> addTodo(TodoDto todoDto,UserDto userDto){
        todoDto.setUser(userDto.toEntity());
        todoRepository.save(todoDto.toEntity());

        return findUserTodos(userDto);
    }

    public List<TodoEntity> updateTodo(Long id){
        TodoEntity target = todoRepository.findById(id).orElse(null);

        assert target != null;
        UserDto userDto = new UserDto(target.getUser());

        target.setDone(!target.isDone());
        todoRepository.save(target);



        return findUserTodos(userDto);
    }

    public List<TodoEntity> deleteTodo(Long id){
        TodoEntity target = todoRepository.findById(id).orElse(null);

        assert target != null;
        UserDto userDto = new UserDto(target.getUser());

        todoRepository.delete(target);

        return findUserTodos(userDto);
    }

}