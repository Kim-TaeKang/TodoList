package com.example.todolist.controller;

import com.example.todolist.user.UserDto;
import com.example.todolist.user.UserEntityRepository;
import com.example.todolist.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
public class UserController {


    private final UserService userService;
    private final UserEntityRepository userEntityRepository;

    public UserController(UserService userService, UserEntityRepository userEntityRepository) {
        this.userService = userService;
        this.userEntityRepository = userEntityRepository;
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        log.info("login page");
        if (session.getAttribute("user")!=null) {
            session.invalidate();
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(HttpSession session,Model model) {
        if(session.getAttribute("reg_Data")==null) {
            UserDto userDto = new UserDto();
            session.setAttribute("reg_Data", userDto);
        }
        model.addAttribute("data", session.getAttribute("reg_Data"));
        log.info("register");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserDto userDto,BindingResult bindingResult,HttpSession session) {
        log.info(userDto.toString());
        session.setAttribute("reg_Data", userDto);

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("User_id")) {
                return "redirect:/register?wrongId";
            }
            if (bindingResult.hasFieldErrors("User_password")) {
                return "redirect:/register?wrongPw";
            }
            return "redirect:/register?validationError";
        }

        if (userService.duplicatedId(userDto.getUser_id())) {
            return "redirect:/register?duplicatedId";
        }
        session.removeAttribute("reg_Data");
        userService.createEntity(userDto);
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String showUser(HttpSession httpSession, Model model) {
        log.info("UserInfo");
        log.info(httpSession.getAttribute("user").toString());
        UserDto userInfo = (UserDto) httpSession.getAttribute("user");
        UserDto userDto = userService.findByNum(userInfo.getUser_num());
        model.addAttribute("user", userDto);
        return "User";
    }

    @GetMapping("/user/{num}/edit")
    public String showUserToEdit(@PathVariable("num") Long num,Model model) {
        UserDto user = userService.findByNum(num);
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping("/user/{num}/edit")
    public String editUser(@PathVariable("num") Long num, @Valid @ModelAttribute UserDto userDto,BindingResult result) {
        if (result.hasFieldErrors("User_password")) {
            return "redirect:/user/{num}/edit?wrongPw";
        }
        userDto.setUser_num(num);
        userService.editEntityByNum(userDto);
        return "redirect:/user";
    }

    @PostMapping("/user/{num}/delete")
    public String deleteUser(@PathVariable("num") Long num) {
        userService.deleteEntityByNum(num);
        return "redirect:/";
    }

    @GetMapping("/user/all")
    public String showAllUser(Model model) {
        model.addAttribute("user", userEntityRepository.findAll());
        return "UserList";
    }

}
