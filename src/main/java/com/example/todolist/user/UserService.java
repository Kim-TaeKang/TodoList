package com.example.todolist.user;

import com.example.todolist.todo.TodoEntity;
import com.example.todolist.todo.TodoEntityRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserService implements UserDetailsService {


    private final UserEntityRepository userRepository;
    private final HttpSession httpSession;
    private final TodoEntityRepository todoEntityRepository;

    public final PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public UserService(UserEntityRepository userRepository, HttpSession httpSession, TodoEntityRepository todoEntityRepository) {
        this.userRepository = userRepository;
        this.httpSession = httpSession;
        this.todoEntityRepository = todoEntityRepository;
    }

    public boolean duplicatedId(String user_id){
        return userRepository.findByUserId(user_id).iterator().hasNext();
    }

    public void createEntity(UserDto userDto){
        System.out.println(userDto.getUser_mail());
        if(!userDto.getUser_mail().isEmpty()){
            userDto.setUser_role(Role.ADMIN);
        }else userDto.setUser_role(Role.USER);
        UserEntity userEntity = userDto.toEntity();
        userRepository.save(userEntity);
    }

    public UserDto findByNum(Long num){
        UserEntity userEntity= userRepository.findByNum(num);
        return new UserDto(userEntity);
    }

    public void editEntityByNum(UserDto userDto){
        UserEntity userEntity = userRepository.findByNum(userDto.getUser_num());
        userEntity.setUser_name(userDto.getUser_name());
        userEntity.setUser_password(userDto.getUser_password());
        userEntity.setUser_mail(userDto.getUser_mail());
        userEntity.setUser_gender(userDto.getUser_gender());
        if(userDto.getUser_mail()!=null) userEntity.setUser_role(Role.ADMIN);
        userRepository.save(userEntity);
    }

    public void deleteEntityByNum(Long num){
        UserEntity userEntity = userRepository.findByNum(num);
        Iterable<TodoEntity> userTodos = todoEntityRepository.findByUser(userEntity);
        if(userTodos!=null)
            todoEntityRepository.deleteAll(userTodos);
        userRepository.deleteById(num);
        httpSession.invalidate();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Processing login user {}", username);
        Iterable<UserEntity> userList = userRepository.findByUserId(username);
        UserEntity userEntity = userList.iterator().next();
        UserDto user = new UserDto(userEntity);
        httpSession.setAttribute("user", user);

        return User.builder().
                username(user.getUser_id()).
                password(passwordEncoder().encode(user.getUser_password())).
                roles(user.getUser_role().toString()).
                build();

//        return new org.springframework.security.core.userdetails.User(
//                user.getUser_id(),
//                passwordEncoder().encode(user.getUser_password()),
//                new ArrayList<>());
    }
}
