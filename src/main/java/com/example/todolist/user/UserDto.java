package com.example.todolist.user;


import jakarta.validation.constraints.Pattern;
import lombok.*;


@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class UserDto {
    private Long User_num;
    private String User_name;
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "ID는 4~20자의 영문과 숫자 조합이어야 합니다.")
    private String User_id;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}$", message = "비밀번호는 6~20자의 영문, 숫자, 특수문자 조합이어야 합니다.")
    private String User_password;
    private String User_mail;
    private Gender User_gender;
    private Role User_role;

    public UserDto(){}

    public UserDto(UserEntity userEntity) {
        User_num = userEntity.getUser_num();
        User_name = userEntity.getUser_name();
        User_id = userEntity.getUser_id();
        User_password = userEntity.getUser_password();
        User_mail = userEntity.getUser_mail();
        User_gender = userEntity.getUser_gender();
        User_role = userEntity.getUser_role();
    }

    public UserEntity toEntity(){   //Entity 생성
        return new UserEntity(
                User_num,
                User_name,
                User_id,
                User_password,
                User_mail,
                User_gender,
                User_role
        );
    }
}
