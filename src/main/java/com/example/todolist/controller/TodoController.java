package com.example.todolist.controller;

import com.example.todolist.todo.TodoDto;
import com.example.todolist.todo.TodoEntity;
import com.example.todolist.todo.TodoEntityRepository;
import com.example.todolist.todo.TodoService;
import com.example.todolist.user.UserDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@Controller
public class TodoController {

    private final TodoService todoService;
    private final TodoEntityRepository todoRepository;

    public TodoController(TodoEntityRepository TodoEntityRepository, TodoService todoService) {
        this.todoRepository = TodoEntityRepository;
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String getHome() {
        return "redirect:/todo";
    }

    @GetMapping("/todo")
    public String TodoHome(Model model, HttpSession session){
        UserDto userDto = (UserDto) session.getAttribute("user");
        if(userDto == null)  return "redirect:/login";

        model.addAttribute("user", userDto);
        log.info("Logined: {}",userDto.getUser_name());

        model.addAttribute("todoList", todoService.findUserTodos(userDto));

        return "todo";
    }

    @PostMapping("/todo-add")
    public String AddTodo(@ModelAttribute TodoDto todoDto, HttpSession session, Model model) {
        log.info("-add todo-");
        UserDto userDto = (UserDto) session.getAttribute("user");

        assert userDto != null;
        model.addAttribute("todoList", todoService.addTodo(todoDto,userDto));

        return "/todo :: #list";
    }

    @PostMapping("/todo-update")
    public String updateTodo(@RequestParam("id") Long id,Model model) {
        log.info("-update todo-");
        model.addAttribute("todoList", todoService.updateTodo(id));
        return "/todo :: #list";
    }

    @PostMapping("/todo-del")
    public String DelTodo(@RequestParam("id") Long id,Model model){
        log.info("-del todo-");
        model.addAttribute("todoList", todoService.deleteTodo(id));
        return "/todo :: #list";
    }



    @PostMapping("/todo/create")
    public String CreateTodo(@RequestParam("TaskName")String task,
                             @RequestParam(value = "Deadline",required = false)
                             @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
                             LocalDate deadline, HttpSession httpSession) {
        log.info("Create Todo");
        UserDto user = (UserDto) httpSession.getAttribute("user");
        TodoDto todoDto = new TodoDto(task,deadline,user);
        TodoEntity todoEntity = todoDto.toEntity();
        todoRepository.save(todoEntity);
        return "redirect:/todo";
    }

    @PostMapping("/todo/{id}/update")
    public String UpdateTodo(@PathVariable("id") Long id) {
        log.info("Update Todo");
        TodoEntity target = todoRepository.findById(id).orElse(null);
        assert target != null;
        target.setDone(!target.isDone());
        todoRepository.save(target);
        log.info(target.toString());
        return "redirect:/todo";
    }

    @PostMapping("/todo/{id}/delete")
    public String DeleteTodo(@PathVariable("id") Long id) {
        log.info("Delete Todo");
        TodoEntity target = todoRepository.findById(id).orElse(null);
        assert target != null;
        todoRepository.delete(target);
        return "redirect:/todo";
    }


    @GetMapping("/todo/all")
    public String AllTodo(Model model) {
        model.addAttribute("todoList", todoRepository.findAll(Sort.by(Sort.Order.asc("deadline"))));
        return "TodoList";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        log.info("access denied page");
        return "accessDenied";
    }
}